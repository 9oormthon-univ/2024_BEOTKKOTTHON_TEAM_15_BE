package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.NewsCheck;

import java.util.List;

public interface NewsCheckService {

    void readNews(Long memberId, Long newsId);

    List<NewsCheck> findByMember(Long memberId);
}
