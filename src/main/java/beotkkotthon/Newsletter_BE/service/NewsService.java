package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NewsService {

    News findById(Long newsId);

    News createNews(Long teamId, MultipartFile imageFile1, MultipartFile imageFile2, NewsSaveRequestDto newsSaveRequestDto) throws IOException;

    List<ShowNewsDto> findNewsByTeam(Long teamId);

    ShowNewsDto getShowNewsDto(Long memberId, Long teamId, Long newsId, int count);

    List<ShowNewsDto> notReadNewslist(Long memberId, Long teamId);

    List<ShowNewsDto> findNewsByMember(Long memberId, Long teamId);

    int countReadMember(Long memberId, Long teamId, Long newsId);

    List<News> findAllNewsByMemberTeam(Long memberId);

    List<ShowNewsDto> findNewsByMemberTeam(Long memberId);

}
