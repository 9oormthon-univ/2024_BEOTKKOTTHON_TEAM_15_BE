package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto.NewsCheckDto;

import java.util.List;

public interface NewsCheckService {

    void readNews(Long memberId, Long newsId);

    List<NewsCheck> findByMember(Long memberId);

    List<NewsCheckDto> findByMemberAndTeamAndNews(Long memberId, Long teamId, Long newsId);
}
