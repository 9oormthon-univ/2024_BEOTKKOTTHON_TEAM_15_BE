package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberSignupRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;


    @PostMapping("/team/{teamId}/news")
    public ApiResponse<NewsResponseDto> createNews(@PathVariable Long teamId, @RequestBody NewsSaveRequestDto newsSaveRequestDto) {
        NewsResponseDto newsResponseDto = newsService.createNews(teamId, newsSaveRequestDto);
        return ApiResponse.onCreate(newsResponseDto);
    }
}
