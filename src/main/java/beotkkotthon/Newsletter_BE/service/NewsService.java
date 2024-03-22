package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NewsService {

    News findById(Long newsId);

    News createNews(Long teamId, NewsSaveRequestDto newsSaveRequestDto) throws IOException;

    List<NewsResponseDto> findNewsByTeam(Long teamId);

    NewsResponseDto.ShowNewsDto getShowNewsDto(Long memberId, Long teamId, Long newsId, int count);

    List<NewsResponseDto> notReadNewslist(Long memberId, Long teamId);

    List<NewsResponseDto> findNewsByMember(Long memberId, Long teamId);

    int countReadMember(Long memberId, Long teamId, Long newsId);

    List<News> findAllNewsByMember(Long memberId, Long teamId);
}
