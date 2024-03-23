package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @CrossOrigin(origins = "https://goormnotification.vercel.app", allowedHeaders = "*")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "NewsCheck")
@RestController
@RequiredArgsConstructor
public class NewsCheckController {

    private final NewsCheckService newsCheckService;
    @GetMapping("news/{newsId}")
    @Operation(summary = "가정통신문 조회 후 확인 버튼 [jwt O]")
    @Parameters({
            @Parameter(name = "newsId", description = "가정통신문의 아이디, path variable 입니다.")
    })
    public ApiResponse<NewsCheckResponseDto.NewsCheckDto> findNewsById(@PathVariable(name = "newsId") Long newsId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        NewsCheckResponseDto.NewsCheckDto newsCheckDto = newsCheckService.readNews(memberId, newsId);
        return ApiResponse.onSuccess(newsCheckDto);
    }
}
