package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.converter.NewsConverter;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final NewsCheckService newsCheckService;

    @PostMapping(value = "/teams/{teamId}/news", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "가정통신문 발행")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<NewsResponseDto> createNews(
            @PathVariable(name = "teamId") Long teamId,
            @RequestPart(value = "writerId", required = true) Long memberId,
            @RequestPart(value = "teamMemberId", required = true) Long teamMemberId,
            @RequestPart(value = "image1", required = false) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        NewsResponseDto newsResponseDto = newsService.createNews(teamId, memberId, teamMemberId, image1, image2, newsSaveRequestDto);
        return ApiResponse.onCreate(newsResponseDto);
    }

    @GetMapping("/teams/news")
    @Operation(summary = "가정통신문 목록 모두 조회(하단그룹-메인)")
    public ApiResponse<NewsResponseDto.ShowNewsListDto> findAllNews(@Parameter(description = "memberId") @RequestPart(name = "memberId") Long memberId) {
        List<News> newsList = newsService.findAll();
        List<NewsCheck> newsCheckList = newsCheckService.findByMember(memberId);
        return ApiResponse.onSuccess(NewsConverter.toShowNewsDtoList(newsList, newsCheckList));
    }

    @GetMapping("/teams/{teamId}/news")
    @Operation(summary = "팀별 가정통신문 조회")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<List<NewsResponseDto>> findNewsByTeam(@PathVariable(name = "teamId") Long teamId) {
        List<NewsResponseDto> newsResponseDtos = newsService.findNewsByTeam(teamId);
        return ApiResponse.onSuccess(newsResponseDtos);
    }

    @GetMapping("/teams/{teamId}/news/{newsId}")
    @Operation(summary = "가정통신문 상세 조회")
    @Parameters({
            @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다."),
            @Parameter(name = "newsId", description = "가정통신문의 아이디, path variable 입니다.")
    })
    public ApiResponse<NewsResponseDto.ShowNewsDto> findNewsById(@Parameter(description = "memberId") @RequestPart(name = "memberId") Long memberId,
                                                                 @PathVariable(name = "teamId") Long teamId,
                                                                 @PathVariable(name = "newsId") Long newsId) {
        newsCheckService.readNews(memberId, newsId);
        return ApiResponse.onSuccess(newsService.getShowNewsDto(teamId, newsId));
    }

    @GetMapping("/news")
    @Operation(summary = "미확인 가정통신문 전체 목록 조회")
    public ApiResponse<List<NewsResponseDto>> notReadNews(@Parameter(description = "memberId") @RequestPart(name = "memberId") Long memberId,
                                                          @RequestParam(name = "teamId", required = false) Long teamId) {
        List<NewsResponseDto> notReadNewsList = newsService.notReadNewslist(memberId, teamId);
        return ApiResponse.onSuccess(notReadNewsList);
    }

    @GetMapping("/{memberId}/news")
    @Operation(summary = "내가 발행한 가정통신문 목록 조회")
    @Parameter(name = "memberId", description = "멤버의 아이디, path variable 입니다.")
    public ApiResponse<List<NewsResponseDto>> findNewsByWriter(@PathVariable(name = "memberId") Long memberId,
                                                               @RequestParam(name = "teamId", required = false) Long teamId) {
        List<NewsResponseDto> newsResponseDtos = newsService.findNewsByMember(memberId, teamId);
        return ApiResponse.onSuccess(newsResponseDtos);
    }
}
