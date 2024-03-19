package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;

import java.util.List;

public interface NewsService {

    NewsResponseDto createNews(Long teamId, NewsSaveRequestDto newsSaveRequestDto);

    List<NewsResponseDto> findNewssByTeam(Long teamId);
}
