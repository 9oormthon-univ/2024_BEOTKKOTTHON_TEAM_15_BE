package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.repository.NewsCheckRepository;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import beotkkotthon.Newsletter_BE.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsCheckServiceImpl implements NewsCheckService {

    private final NewsCheckRepository newsCheckRepository;
    private final MemberService memberService;
    private final NewsService newsService;

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
}
