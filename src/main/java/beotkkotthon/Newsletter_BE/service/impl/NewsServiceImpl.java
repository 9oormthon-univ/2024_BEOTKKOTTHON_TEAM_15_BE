package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.converter.NewsConverter;
import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;

import beotkkotthon.Newsletter_BE.repository.NewsCheckRepository;
import beotkkotthon.Newsletter_BE.repository.MemberRepository;
import beotkkotthon.Newsletter_BE.repository.NewsRepository;
import beotkkotthon.Newsletter_BE.service.*;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final ImageUploadService imageUploadService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final NewsCheckRepository newsCheckRepository;

    @Override
    public News findById(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(
                () -> new GeneralException(ErrorStatus.NEWS_NOT_FOUND));
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAll();
    }

    @Transactional
    @Override
    public NewsResponseDto createNews(Long teamId, Long teamMemberId, MultipartFile image1, MultipartFile image2, NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        Team team = teamService.findById(teamId);
        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Member writer = memberService.findById(loginMemberId);
        Member teamMember = memberService.findById(teamMemberId);
        String imageUrl1 = imageUploadService.uploadImage(image1);
        String imageUrl2 = imageUploadService.uploadImage(image2);

        News news = newsSaveRequestDto.toEntity(writer, team, imageUrl1, imageUrl2);
        newsRepository.save(news);

//        나중에 멤버팀 테이블에서 멤버 리스트 불러옴 -> 각 멤버의 NewsCheck 테이블 생성
        setNewsCheck(teamMember, news);
        return new NewsResponseDto(news);
    }

    private void setNewsCheck(Member member, News news) {
        NewsCheck newsCheck = NewsCheck.NewsCheckCreateBuilder().news(news).member(member).build();
        newsCheckRepository.save(newsCheck);
    }

    @Transactional(readOnly = true)
    @Override
    public List<NewsResponseDto> findNewsByTeam(Long teamId) {

        Team team = teamService.findById(teamId);
        List<NewsResponseDto> newsResponseDtos = team.getNewsList().stream().map(NewsResponseDto::new)
                .sorted(Comparator.comparing(NewsResponseDto::getId, Comparator.reverseOrder()))  // id 내림차순 정렬 후 (최신 생성순)
                .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))  // 수정날짜 내림차순 정렬 (최신 수정순)
                .collect(Collectors.toList());  // 정렬 완료한 리스트 반환 (연속sorted 정렬은 마지막 순서의 기준이 가장 주요된 기준임.)

        return newsResponseDtos;
    }

    @Override
    public NewsResponseDto.ShowNewsDto getShowNewsDto(Long teamId, Long newsId) {
        News news = findById(newsId);
        return NewsConverter.toShowNewsDto(news);
    }

    @Override
    public List<NewsResponseDto> notReadNewslist(Long memberId, Long teamId) {
        Member member = memberService.findById(memberId);
        List<NewsResponseDto> notReadNewsDtos;

        if (teamId != null) {
            Team team = teamService.findById(teamId);
            notReadNewsDtos = team.getNewsList().stream()
                    .filter(news -> {
                        NewsCheck newsCheck = newsCheckRepository.findByMemberAndNews(member, news).orElse(null);
                        return newsCheck != null && newsCheck.getCheckStatus() == CheckStatus.NOT_READ;
                    })
                    .map(NewsResponseDto::new)
                    .sorted(Comparator.comparing(NewsResponseDto::getId))
                    .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            notReadNewsDtos = newsCheckRepository.findByMember(member).stream()
                    .filter(newsCheck -> newsCheck.getCheckStatus() == CheckStatus.NOT_READ)
                    .map(newsCheck -> new NewsResponseDto(newsCheck.getNews()))
                    .sorted(Comparator.comparing(NewsResponseDto::getId))
                    .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return notReadNewsDtos;
    }

    @Override
    public List<NewsResponseDto> findNewsByMember(Long memberId, Long teamId) {
        Member member = memberService.findById(memberId);

        List<NewsResponseDto> newsResponseDtos;

        if (teamId != null) {
            Team team = teamService.findById(teamId);
            newsResponseDtos = team.getNewsList().stream()
                    .filter(news -> news.getMember().getId().equals(member.getId()))
                    .map(NewsResponseDto::new)
                    .sorted(Comparator.comparing(NewsResponseDto::getId))
                    .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            List<News> newsList = newsRepository.findAll().stream()
                    .filter(news -> news.getMember().getId().equals(member.getId()))
                    .collect(Collectors.toList());

            newsResponseDtos = newsList.stream()
                    .map(NewsResponseDto::new)
                    .sorted(Comparator.comparing(NewsResponseDto::getId))
                    .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return newsResponseDtos;
    }
}