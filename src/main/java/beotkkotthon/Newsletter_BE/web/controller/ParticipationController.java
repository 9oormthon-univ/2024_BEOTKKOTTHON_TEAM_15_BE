package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.ParticipationService;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.ParticipationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;


    @GetMapping("/teams/{teamId}/participations")
    public ApiResponse<List<ParticipationResponseDto>> findParticipationByTeam(@PathVariable Long teamId) {
        List<ParticipationResponseDto> participationResponseDtoList = participationService.findParticipationByTeam(teamId);
        return ApiResponse.onSuccess(participationResponseDtoList);
    }
}
