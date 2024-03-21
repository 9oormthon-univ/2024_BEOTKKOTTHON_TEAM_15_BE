package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.converter.NewsCheckConverter;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.repository.NewsCheckRepository;
import beotkkotthon.Newsletter_BE.service.*;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto.NewsCheckDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsCheckServiceImpl implements NewsCheckService {

    private final NewsCheckRepository newsCheckRepository;
    private final MemberService memberService;
    private final NewsService newsService;
    private final TeamService teamService;

    @Transactional
    @Override
    public void readNews(Long memberId, Long newsId) {
        Member member = memberService.findById(memberId);
        News news = newsService.findById(newsId);

        Optional<NewsCheck> newsCheck = newsCheckRepository.findByMemberAndNews(member, news);

        if (newsCheck.isPresent()) {
            NewsCheck newNewsCheck = newsCheck.get();
            newNewsCheck.updateStatus(CheckStatus.READ);
            newsCheckRepository.save(newNewsCheck);
        }
    }

    @Override
    public List<NewsCheck> findByMember(Long memberId) {
        Member member = memberService.findById(memberId);
        return newsCheckRepository.findByMember(member);
    }

    //공지 확인/미확인 리스트와 인원수
    @Override
    public List<NewsCheckDto> findByMemberAndTeamAndNews(Long memberId, Long teamId, Long newsId) {
        News news = newsService.findById(newsId);

        List<NewsCheck> newsChecks = newsCheckRepository.findByNews(news);

        return newsChecks.stream()
                .map(NewsCheckConverter::toNewsCheckDto)
                .collect(Collectors.toList());
    }
}
