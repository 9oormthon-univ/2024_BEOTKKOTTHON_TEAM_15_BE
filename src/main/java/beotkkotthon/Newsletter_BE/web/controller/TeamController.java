package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.request.NewsSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;


    @GetMapping("/team/{teamId}/news")
    public ApiResponse<List<NewsResponseDto>> findNewssByTeam(@PathVariable Long teamId) {
        List<NewsResponseDto> newsResponseDtos = teamService.findNewssByTeam(teamId);
        return ApiResponse.onSuccess(newsResponseDtos);
    }
}
