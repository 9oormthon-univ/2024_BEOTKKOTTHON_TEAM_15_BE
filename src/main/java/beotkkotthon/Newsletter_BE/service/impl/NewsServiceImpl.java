package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;

import beotkkotthon.Newsletter_BE.repository.NewsCheckRepository;
import beotkkotthon.Newsletter_BE.repository.NewsRepository;
import beotkkotthon.Newsletter_BE.service.*;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final ImageUploadService imageUploadService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final NewsCheckRepository newsCheckRepository;
    private final MemberTeamService memberTeamService;
    private final NotificationService notificationService;


    @Override
    public News findById(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(
                () -> new GeneralException(ErrorStatus.NEWS_NOT_FOUND));
    }

    @Transactional
    @Override
    public NewsResponseDto createNews(Long teamId, MultipartFile image1, MultipartFile image2, NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        Team team = teamService.findById(teamId);

        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Member writer = memberService.findById(loginMemberId);
        MemberTeam loginMemberTeam = memberTeamService.findByMemberAndTeam(writer, team);
        Role loginRole = loginMemberTeam.getRole();

        String imageUrl1 = imageUploadService.uploadImage(image1);
        String imageUrl2 = imageUploadService.uploadImage(image2);

        if (loginRole.equals(Role.LEADER) || loginRole.equals(Role.CREATOR)) {
            News news = newsSaveRequestDto.toEntity(writer, team, imageUrl1, imageUrl2);
            newsRepository.save(news);

            List<MemberTeam> memberTeams = memberTeamService.findAllByTeam(team);
            for (MemberTeam memberTeam : memberTeams) {
                setNewsCheck(memberTeam.getMember(), news);

                // 가정통신문 발행자를 제외한 나머지 그룹원들에게 fcm 푸시 알림 발송.
                String title = "공지 발생", message = "'" + team.getName() + "' 그룹의 공지가 올라왔습니다.";
                Optional<NotificationDto> opNotificationDto = notificationService.makeMessage(memberTeam.getMember().getId(), title, message);
                if(memberTeam.getMember().getId() != loginMemberId && opNotificationDto.isPresent()) {
                    NotificationDto notificationDto = opNotificationDto.get();
                    try {
                        notificationService.sendNotification(notificationDto);
                    }
                    catch (ExecutionException | InterruptedException ex) {
                        throw new GeneralException(ErrorStatus.INTERNAL_ERROR, ex.getMessage());
                    }
                }
            }

            return new NewsResponseDto(news);
        } else {
            throw new GeneralException(ErrorStatus.NOT_AUTHORIZED, "리더 권한 없음");
        }
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
    public ShowNewsDto getShowNewsDto(Long memberId, Long teamId, Long newsId, int count) {
        News news = findById(newsId);
        Team team = teamService.findById(teamId);
        count = countReadMember(memberId, teamId, newsId);

        return ShowNewsDto.builder()
                .newsId(news.getId())
                .title(news.getTitle())
                .writer(news.getMember().getUsername())
                .content(news.getContent())
                .imageUrl1(news.getImageUrl1())
                .imageUrl2(news.getImageUrl2())
                .limitTime(news.getLimitTime())
                .readMemberCount(count)
                .notReadMemberCount(team.getTeamSize() - count)
                .build();
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

    //가입한 팀의 모든공지, ?teamId=1 팀별 공지 목록 조회
    @Override
    public List<News> findAllNewsByMember(Long memberId, Long teamId) {
        Member member = memberService.findById(memberId);

        if (teamId != null) {
            Team team = teamService.findById(teamId);
            if (team == memberTeamService.findByMemberAndTeam(member, team).getTeam()) {
                if (member.getMemberTeamList().stream().anyMatch(mt -> mt.getTeam().getId().equals(teamId))) {
                    return team.getNewsList().stream()
                            .sorted(Comparator.comparing(News::getId))
                            .sorted(Comparator.comparing(News::getModifiedTime, Comparator.reverseOrder()))
                            .collect(Collectors.toList());
                }
            } else {
                throw new GeneralException(ErrorStatus.MEMBERTEAM_NOT_FOUND);
            }
        } else {
            List<Team> teams = member.getMemberTeamList().stream()
                    .map(MemberTeam::getTeam)
                    .collect(Collectors.toList());

            return teams.stream()
                    .flatMap(team -> team.getNewsList().stream())
                    .sorted(Comparator.comparing(News::getId))
                    .sorted(Comparator.comparing(News::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

        }
        throw new GeneralException(ErrorStatus.TEAM_NOT_FOUND);
    }

    @Override
    public int countReadMember(Long memberId, Long teamId, Long newsId) {
        Member member = memberService.findById(memberId);
        Team team = teamService.findById(teamId);
        News news = findById(newsId);
        List<NewsCheck> newsChecks = newsCheckRepository.findByNews(news);

        int checkedCount = 0;

        MemberTeam loginMemberTeam = memberTeamService.findByMemberAndTeam(member, team);
        Role loginRole = loginMemberTeam.getRole();

        if (loginRole.equals(Role.LEADER) || loginRole.equals(Role.CREATOR)) {
            checkedCount = (int) newsChecks.stream()
                    .filter(newsCheck -> newsCheck.getCheckStatus() == CheckStatus.READ)
                    .count();
        }
        return checkedCount;
    }
}