package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.request.TeamSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;


    @PostMapping("/teams")
    public ApiResponse<TeamResponseDto> createTeam(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart TeamSaveRequestDto teamSaveRequestDto) throws IOException {

        TeamResponseDto teamResponseDto = teamService.createTeam(image, teamSaveRequestDto);
        return ApiResponse.onCreate(teamResponseDto);
    }

    @GetMapping("/teams")
    public ApiResponse<List<TeamResponseDto>> findTeamsByMember(  // 그룹명 검색 or 초대링크 클릭 or 내가 가입한 팀 목록 조회
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "link", required = false) String link) {  // 이번 해커톤에서는 MVP로써, link는 사용하지 않을 예정임.

        List<TeamResponseDto> teamResponseDtos = teamService.findTeamsByMember(name, link);
        return ApiResponse.onSuccess(teamResponseDtos);
    }
}
