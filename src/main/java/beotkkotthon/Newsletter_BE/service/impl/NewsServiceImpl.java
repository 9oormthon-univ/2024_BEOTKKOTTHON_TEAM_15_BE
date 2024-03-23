package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;

import beotkkotthon.Newsletter_BE.converter.NewsConverter;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;

import beotkkotthon.Newsletter_BE.repository.MemberTeamRepository;
import beotkkotthon.Newsletter_BE.repository.NewsCheckRepository;
import beotkkotthon.Newsletter_BE.repository.NewsRepository;
import beotkkotthon.Newsletter_BE.service.*;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final ImageUploadService imageUploadService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final NewsCheckRepository newsCheckRepository;
    private final MemberTeamService memberTeamService;
    private final NotificationService notificationService;
    private final MemberTeamRepository memberTeamRepository;

    @Override
    public News findById(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(
                () -> new GeneralException(ErrorStatus.NEWS_NOT_FOUND));
    }

    @Transactional
    @Override
    public News createNews(Long teamId, MultipartFile imageFile1, MultipartFile imageFile2, NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        Team team = teamService.findById(teamId);

        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Member writer = memberService.findById(loginMemberId);
        MemberTeam loginMemberTeam = memberTeamService.findByMemberAndTeam(writer, team);
        Role loginRole = loginMemberTeam.getRole();

        String imageUrl1 = imageUploadService.uploadImage(imageFile1);
        String imageUrl2 = imageUploadService.uploadImage(imageFile2);

        if (loginRole.equals(Role.LEADER) || loginRole.equals(Role.CREATOR)) {
            News news = newsSaveRequestDto.toEntity(writer, team, newsSaveRequestDto.getMinute(), imageUrl1, imageUrl2);
            newsRepository.save(news);

            List<MemberTeam> memberTeams = memberTeamService.findAllByTeam(team);
            for (MemberTeam memberTeam : memberTeams) {
                setNewsCheck(memberTeam.getMember(), news);

                // 가정통신문 발행자를 제외한 나머지 그룹원들에게 fcm 푸시 알림 발송.
                String title = "새 공지가 등록되었습니다.", message = "'" + team.getName() + "' 그룹의 새 공지를 확인해주세요.";
                Optional<NotificationDto> opNotificationDto = notificationService.makeMessage(memberTeam.getMember().getId(), title, message);
                if(memberTeam.getMember().getId() != loginMemberId && opNotificationDto.isPresent()) {
                    NotificationDto notificationDto = opNotificationDto.get();
                    try {
                        notificationService.sendNotification(memberTeam.getMember().getId(), notificationDto);
                    }
                    catch (ExecutionException | InterruptedException ex) {
                        throw new GeneralException(ErrorStatus.INTERNAL_ERROR, ex.getMessage());
                    }
                }
            }
            return news;
        } else {
            throw new GeneralException(ErrorStatus.NOT_AUTHORIZED, "리더 권한 없음");
        }
    }

    private void setNewsCheck(Member member, News news) {
        NewsCheck newsCheck = NewsCheck.NewsCheckCreateBuilder().news(news).member(member).build();
        newsCheckRepository.save(newsCheck);
    }

    @Override
    public List<ShowNewsDto> findNewsByTeam(Long teamId) {
        Team team = teamService.findById(teamId);
        team.getNewsList().forEach(news -> news.getMember());

        List<ShowNewsDto> showNewsDtos = team.getNewsList().stream().map(NewsConverter::toShowNewsDto)
                .sorted(Comparator.comparing(ShowNewsDto::getId, Comparator.reverseOrder()))
                .sorted(Comparator.comparing(ShowNewsDto::getModifiedTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return showNewsDtos;
    }

    //가입한 팀의 모든공지, ?teamId=1 팀별 공지 목록 조회
    @Override
    public List<News> findAllNewsByMemberTeam(Long memberId) {
        Member member = memberService.findById(memberId);
        List<MemberTeam> memberTeamList = memberTeamRepository.findAllByMember(member);

        return memberTeamList.stream()
                .map(MemberTeam::getTeam)
                .flatMap(team -> team.getNewsList().stream())
                .collect(Collectors.toList());
    }

    @Override
    public ShowNewsDto getShowNewsDto(Long memberId, Long teamId, Long newsId, int count) {
        News news = findById(newsId);
        Team team = teamService.findById(teamId);
        Member member = memberService.findById(memberId);
        Optional<NewsCheck> newsCheck = newsCheckRepository.findByMemberAndNews(member, news);
        count = countReadMember(memberId, teamId, newsId);

        return ShowNewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .teamId(news.getTeam().getId())
                .teamName(news.getTeam().getName())
                .writer(news.getMember().getUsername())
                .content(news.getContent())
                .imageUrl1(news.getImageUrl1())
                .imageUrl2(news.getImageUrl2())
                .limitTime(news.getLimitTime())
                .checkStatus(newsCheck.get().getCheckStatus())
                .readMemberCount(count)
                .notReadMemberCount(team.getTeamSize() - count)
                .build();
    }

    @Override
    public List<ShowNewsDto> notReadNewslist(Long memberId, Long teamId) {
        Member member = memberService.findById(memberId);
        List<ShowNewsDto> notReadNewsDtos;

        if(teamId != null) {
            Team team = teamService.findById(teamId);
            notReadNewsDtos = team.getNewsList().stream()
                    .filter(news -> {
                        NewsCheck newsCheck = newsCheckRepository.findByMemberAndNews(member, news).orElse(null);
                        return newsCheck.getCheckStatus().equals(CheckStatus.NOT_READ);
                    })
                    .map(news -> {
                        news.getMember();
                        return NewsConverter.toShowNewsDto(news);
                    })
                    .sorted(Comparator.comparing(ShowNewsDto::getId))
                    .sorted(Comparator.comparing(ShowNewsDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            notReadNewsDtos = newsCheckRepository.findByMember(member).stream()
                    .filter(newsCheck -> newsCheck.getCheckStatus() == CheckStatus.NOT_READ)
                    .map(newsCheck -> NewsConverter.toShowNewsDto(newsCheck.getNews()))
                    .sorted(Comparator.comparing(ShowNewsDto::getId))
                    .sorted(Comparator.comparing(ShowNewsDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return notReadNewsDtos;
    }

    @Override
    public List<ShowNewsDto> findNewsByMember(Long memberId, Long teamId) {
        Member member = memberService.findById(memberId);

        List<ShowNewsDto> showNewsDtos;

        if (teamId != null) {
            Team team = teamService.findById(teamId);
            showNewsDtos = team.getNewsList().stream()
                    .filter(news -> news.getMember().getId().equals(member.getId()))
                    .map(NewsConverter::toShowNewsDto)
                    .sorted(Comparator.comparing(ShowNewsDto::getId))
                    .sorted(Comparator.comparing(ShowNewsDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            List<News> newsList = newsRepository.findAll().stream()
                    .filter(news -> news.getMember().getId().equals(member.getId()))
                    .collect(Collectors.toList());

            showNewsDtos = newsList.stream()
                    .map(NewsConverter::toShowNewsDto)
                    .sorted(Comparator.comparing(ShowNewsDto::getId))
                    .sorted(Comparator.comparing(ShowNewsDto::getModifiedTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return showNewsDtos;
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

        if (!loginRole.equals(Role.MEMBER)) {
            checkedCount = (int) newsChecks.stream()
                    .filter(newsCheck -> newsCheck.getCheckStatus().equals(CheckStatus.READ))
                    .count();
        }
        return checkedCount;
    }
}