package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto.NewsCheckDto;

import java.util.List;

public interface NewsCheckService {

    NewsCheckDto readNews(Long memberId, Long newsId);

    List<NewsCheckDto> findByNews(Long memberId, Long newsId);
}
