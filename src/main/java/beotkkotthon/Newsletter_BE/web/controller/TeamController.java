package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.converter.TeamConverter;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.NewsService;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.request.TeamSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;
import beotkkotthon.Newsletter_BE.web.dto.response.TeamResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "https://goormnotlfication.vercel.app", allowedHeaders = "*")
@Tag(name = "Team")
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final NewsService newsService;


    @PostMapping(value = "/teams", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "CREATOR로써 그룹 생성 [jwt O]")
    public ApiResponse<TeamResponseDto.TeamDto> createTeam(@RequestPart(value="imageFile") MultipartFile imageFile, @RequestPart(value="teamSaveRequestDto") TeamSaveRequestDto teamSaveRequestDto) throws IOException {
        Team team = teamService.createTeam(SecurityUtil.getCurrentMemberId(), teamSaveRequestDto, imageFile);
        return ApiResponse.onCreate(TeamConverter.toTeamResultDto(team));
    }

    @GetMapping("/teams")
    @Operation(summary = "?name=test nameO linkX 그룹명 검색 / nameX linkO 초대링크 클릭 / nameX linkX 내가 가입한 팀 목록 조회 [jwt O]")
    public ApiResponse<List<TeamResponseDto>> findTeamsByMember(  // 그룹명 검색 or 초대링크 클릭 or 내가 가입한 팀 목록 조회
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "link", required = false) String link) {  // 이번 해커톤에서는 MVP로써, link는 사용하지 않을 예정임.

        List<TeamResponseDto> teamResponseDtos = teamService.findTeamsByMember(name, link);
        return ApiResponse.onSuccess(teamResponseDtos);
    }

    @GetMapping("/teams/{teamId}")
    @Operation(summary = "개별 팀 조회 [jwt O]")
    public ApiResponse<TeamResponseDto.ShowTeamDto> findTeamById(@PathVariable(name = "teamId") Long teamId) {
        List<ShowNewsDto> newsList = newsService.findNewsByTeam(teamId);
        return ApiResponse.onSuccess(teamService.showTeamById(SecurityUtil.getCurrentMemberId(), teamId, newsList));
    }
}
