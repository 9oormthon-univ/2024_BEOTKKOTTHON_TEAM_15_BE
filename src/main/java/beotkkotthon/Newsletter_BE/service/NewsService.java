package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NewsService {

    News findById(Long newsId);

    List<News> findAll();

    NewsResponseDto createNews(Long teamId, Long memberId, Long teamMemberId, MultipartFile image1, MultipartFile image2, NewsSaveRequestDto newsSaveRequestDto) throws IOException;

    List<NewsResponseDto> findNewsByTeam(Long teamId);

    NewsResponseDto.ShowNewsDto getShowNewsDto(Long teamId, Long newsId);

    List<NewsResponseDto> notReadNewslist(Long memberId, Long teamId);
}
