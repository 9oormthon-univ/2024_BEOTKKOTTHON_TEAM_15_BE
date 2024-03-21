package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.ParticipationService;
import beotkkotthon.Newsletter_BE.web.dto.request.ParticipationRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.ParticipationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Participation")
@RestController
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;

    @GetMapping("/teams/{teamId}/participations")
    @Operation(summary = "그룹의 참여신청자 목록 조회 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<List<ParticipationResponseDto>> findParticipationByTeam(@PathVariable Long teamId) {
        List<ParticipationResponseDto> participationResponseDtoList = participationService.findParticipationByTeam(teamId);
        return ApiResponse.onSuccess(participationResponseDtoList);
    }

    @PostMapping("/teams/{teamId}/participations")  // RequestBody가 필요하기도 했고, 서비스 클래스에서 DB save 로직도 있으므로, @PostMapping을 사용.
    @Operation(summary = "그룹의 참여신청자 수락/거절 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse acceptParticipation(@PathVariable Long teamId, @RequestBody ParticipationRequestDto participationRequestDto) {  // 새멤버 수락/거절 결과 Participation에서 해당 데이터 삭제 후, 수락일 경우 MemberTeam에도 추가.
        participationService.acceptParticipation(teamId, participationRequestDto);
        return ApiResponse.onSuccess(null);  // 반환값이 없지만 PostMapping 이므로, onUpdateDelete(null);말고 onSuccess(null);로 사용.
    }

    @PostMapping("/teams/{teamId}")
    @Operation(summary = "현재 로그인된 사용자가 그룹에 참여신청 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<ParticipationResponseDto> createParticipation(@PathVariable Long teamId, @RequestParam(value = "requestrole", required = true) String requestRole) {
        ParticipationResponseDto participationResponseDto = participationService.createParticipation(teamId, requestRole);
        return ApiResponse.onSuccess(participationResponseDto);
    }
}
