package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.TeamSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
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
}
