package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.converter.NewsConverter;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.NewsCheck;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@Tag(name = "News")
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final NewsCheckService newsCheckService;

    @PostMapping(value = "/teams/{teamId}/news", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "가정통신문 발행 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<NewsResponseDto> createNews(
            @PathVariable(name = "teamId") Long teamId,
            @RequestPart(value = "image1", required = false) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        NewsResponseDto newsResponseDto = newsService.createNews(teamId, image1, image2, newsSaveRequestDto);
        return ApiResponse.onCreate(newsResponseDto);
    }

    @GetMapping("/teams/news")
    @Operation(summary = "가정통신문 목록 모두 조회(하단그룹-메인) [jwt O]")
    public ApiResponse<NewsResponseDto.ShowNewsListDto> findAllNews(@RequestParam(value = "teamId", required = false) Long teamId){
        List<News> newsList = newsService.findAllNewsByMember(SecurityUtil.getCurrentMemberId(), teamId);
        List<NewsCheck> newsCheckList = newsCheckService.findByMember(SecurityUtil.getCurrentMemberId());
        return ApiResponse.onSuccess(NewsConverter.toShowNewsDtoList(newsList, newsCheckList));
    }

    @GetMapping("/teams/{teamId}/news")
    @Operation(summary = "팀별 가정통신문 조회 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<List<NewsResponseDto>> findNewsByTeam(@PathVariable(name = "teamId") Long teamId) {
        List<NewsResponseDto> newsResponseDtos = newsService.findNewsByTeam(teamId);
        return ApiResponse.onSuccess(newsResponseDtos);
    }

    @GetMapping("/teams/{teamId}/news/{newsId}")
    @Operation(summary = "가정통신문 상세 조회와 인원수 [jwt O]")
    @Parameters({
            @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다."),
            @Parameter(name = "newsId", description = "가정통신문의 아이디, path variable 입니다.")
    })
    public ApiResponse<NewsResponseDto.NewsDetailDto> findNewsById(@PathVariable(name = "teamId") Long teamId,
                                                                 @PathVariable(name = "newsId") Long newsId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        newsCheckService.readNews(memberId, newsId);
        NewsResponseDto.ShowNewsDto showNewsDto = newsService.getShowNewsDto(memberId, teamId, newsId, 0);
        List<NewsCheckResponseDto.NewsCheckDto> newsCheckDto = newsCheckService.findByMemberAndTeamAndNews(memberId, teamId, newsId);
        return ApiResponse.onSuccess(new NewsResponseDto.NewsDetailDto(showNewsDto, newsCheckDto));
    }

    @GetMapping("/news")
    @Operation(summary = "미확인 가정통신문 전체 목록 조회 [jwt O]")
    public ApiResponse<List<NewsResponseDto>> notReadNews(@RequestParam(name = "teamId", required = false) Long teamId) {
        List<NewsResponseDto> notReadNewsList = newsService.notReadNewslist(SecurityUtil.getCurrentMemberId(), teamId);
        return ApiResponse.onSuccess(notReadNewsList);
    }

    @GetMapping("/mynews")
    @Operation(summary = "내가 발행한 가정통신문 목록 조회 [jwt O]")
    public ApiResponse<List<NewsResponseDto>> findNewsByWriter(@RequestParam(name = "teamId", required = false) Long teamId) {
        List<NewsResponseDto> newsResponseDtos = newsService.findNewsByMember(SecurityUtil.getCurrentMemberId(), teamId);
        return ApiResponse.onSuccess(newsResponseDtos);
    }
}
