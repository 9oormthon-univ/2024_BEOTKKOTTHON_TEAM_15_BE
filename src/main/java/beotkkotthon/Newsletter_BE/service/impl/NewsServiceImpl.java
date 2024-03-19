package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.converter.NewsConverter;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.NewsRepository;
import beotkkotthon.Newsletter_BE.service.ImageUploadService;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.service.TeamService;
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

    @Override
    public News findById(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(
                () -> new GeneralException(ErrorStatus.NEWS_NOT_FOUND));
    }

    @Override
    public List<News> findAllNews() {
        return newsRepository.findAll();
    }

    @Transactional
    @Override
    public NewsResponseDto createNews(Long teamId, Long memberId, MultipartFile image1, MultipartFile image2, NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        Team team = teamService.findById(teamId);
        Member member = memberService.findById(memberId);
        String imageUrl1 = imageUploadService.uploadImage(image1);
        String imageUrl2 = imageUploadService.uploadImage(image2);
        News news = newsSaveRequestDto.toEntity(member, team, imageUrl1, imageUrl2);
        newsRepository.save(news);

        return new NewsResponseDto(news);
    }

    @Transactional(readOnly = true)
    @Override
    public List<NewsResponseDto> findNewsByTeam(Long teamId) {

        Team team = teamService.findById(teamId);
        List<NewsResponseDto> newsResponseDtos = team.getNewsList().stream().map(NewsResponseDto::new)
                .sorted(Comparator.comparing(NewsResponseDto::getModifiedTime, Comparator.reverseOrder()))  // 수정시각 기준 내림차순 정렬
                .collect(Collectors.toList());

        return newsResponseDtos;
    }

    @Override
    public NewsResponseDto.ShowNewsDto getShowNewsDto(Long teamId, Long newsId) {
        News news = findById(newsId);
        return NewsConverter.toShowNewsDto(news);
    }
}
