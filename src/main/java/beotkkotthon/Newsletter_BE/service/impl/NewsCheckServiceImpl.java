package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.converter.NewsCheckConverter;
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
import beotkkotthon.Newsletter_BE.service.*;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto.NewsCheckDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NewsCheckServiceImpl implements NewsCheckService {

    private final NewsCheckRepository newsCheckRepository;
    private final MemberService memberService;
    private final NewsService newsService;
    private final MemberTeamService memberTeamService;

    @Transactional
    @Override
    public NewsCheckDto readNews(Long memberId, Long newsId) {
        Member member = memberService.findById(memberId);
        News news = newsService.findById(newsId);

        Optional<NewsCheck> newsCheck = newsCheckRepository.findByMemberAndNews(member, news);

        if (newsCheck.isPresent()) {
            NewsCheck newNewsCheck = newsCheck.get();
            newNewsCheck.updateStatus(CheckStatus.READ);
            newsCheckRepository.save(newNewsCheck);
        }
        return NewsCheckResponseDto.NewsCheckDto.builder()
                .checkStatus(newsCheck.get().getCheckStatus())
                .username(member.getUsername())
                .newsId(newsId)
                .checkTime(LocalDateTime.now())
                .build();
    }

    @Override
    public List<NewsCheckDto> findByNews(Long memberId, Long newsId) {
        Member member = memberService.findById(memberId);
        News news = newsService.findById(newsId);
        Team team = news.getTeam();

        MemberTeam loginMemberTeam = memberTeamService.findByMemberAndTeam(member, team);
        Role loginRole = loginMemberTeam.getRole();

        List<NewsCheck> newsChecks = newsCheckRepository.findByNews(news);
        newsChecks.forEach(NewsCheck::getMember);

        if (!loginRole.equals(Role.MEMBER)) {
            return newsChecks.stream()
                    .map(NewsCheckConverter::toNewsCheckDto)
                    .collect(Collectors.toList());
        } else {
            throw new GeneralException(ErrorStatus.NOT_AUTHORIZED);
        }
    }
}
