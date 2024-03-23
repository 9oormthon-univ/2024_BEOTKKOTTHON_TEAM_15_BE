package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.converter.NewsConverter;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsCheckResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsListDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.NewsDetailDto;
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

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "News")
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final NewsCheckService newsCheckService;

    // !!! 임시 에러 해결. 차후 수정 반드시 필요 !!!
    @PostMapping(value = "/teams/{teamId}/news", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "가정통신문 발행 & 발행자 제외한 그룹 전인원에게 푸시 알림 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<ShowNewsDto> createNews(
            @PathVariable(name = "teamId") Long teamId,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "newsSaveRequestDto") NewsSaveRequestDto newsSaveRequestDto) throws IOException {
        MultipartFile imageFile1 = null, imageFile2 = null;
        if(imageFiles.size() == 1) imageFile1 = imageFiles.get(0);
        else if(imageFiles.size() == 2) {
            imageFile1 = imageFiles.get(0);
            imageFile2 = imageFiles.get(1);
        }
        News news = newsService.createNews(teamId, imageFile1, imageFile2, newsSaveRequestDto);
        return ApiResponse.onCreate(NewsConverter.toShowNewsDto(news));
    }

    @GetMapping("/teams/news")
    @Operation(summary = "가정통신문 목록 모두 조회(하단그룹-메인) [jwt O]")
    public ApiResponse<ShowNewsListDto> findAllNews(@RequestParam(value = "teamId", required = false) Long teamId){
        List<News> newsList = newsService.findAllNewsByMemberTeam(SecurityUtil.getCurrentMemberId(), teamId);
        return ApiResponse.onSuccess(NewsConverter.toShowNewsDtoList(newsList));
    }

    @GetMapping("/teams/{teamId}/news")
    @Operation(summary = "팀별 가정통신문 조회 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<List<ShowNewsDto>> findNewsByTeam(@PathVariable(name = "teamId") Long teamId) {
        List<ShowNewsDto> newsResponseDtos = newsService.findNewsByTeam(teamId);
        return ApiResponse.onSuccess(newsResponseDtos);
    }

    @GetMapping("/teams/{teamId}/news/{newsId}")
    @Operation(summary = "가정통신문 상세 조회와 인원수 [jwt O]")
    @Parameters({
            @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다."),
            @Parameter(name = "newsId", description = "가정통신문의 아이디, path variable 입니다.")
    })
    public ApiResponse<NewsDetailDto> findNewsById(@PathVariable(name = "teamId") Long teamId,
                                                                 @PathVariable(name = "newsId") Long newsId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        ShowNewsDto showNewsDto = newsService.getShowNewsDto(memberId, teamId, newsId, 0);
        List<NewsCheckResponseDto.NewsCheckDto> newsCheckDto = newsCheckService.findByMemberAndTeamAndNews(memberId, teamId, newsId);
        return ApiResponse.onSuccess(new NewsResponseDto.NewsDetailDto(showNewsDto, newsCheckDto));
    }

    @GetMapping("/news")
    @Operation(summary = "미확인 가정통신문 전체/팀별 목록 조회 + ?teamId=1 [jwt O] +")
    public ApiResponse<List<ShowNewsDto>> notReadNews(@RequestParam(name = "teamId", required = false) Long teamId) {
        List<ShowNewsDto> notReadNewsList = newsService.notReadNewslist(SecurityUtil.getCurrentMemberId(), teamId);
        return ApiResponse.onSuccess(notReadNewsList);
    }

    @GetMapping("/mynews")
    @Operation(summary = "내가 발행한 가정통신문 전체/팀별 목록 조회 + ?teamId=1[jwt O]")
    public ApiResponse<List<ShowNewsDto>> findNewsByWriter(@RequestParam(name = "teamId", required = false) Long teamId) {
        List<ShowNewsDto> newsResponseDtos = newsService.findNewsByMember(SecurityUtil.getCurrentMemberId(), teamId);
        return ApiResponse.onSuccess(newsResponseDtos);
    }
}
