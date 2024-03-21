package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.MemberTeamService;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Tag(name = "MemberTeam")
@RestController
@RequiredArgsConstructor
public class MemberTeamController {

    private final MemberTeamService memberTeamService;


    @DeleteMapping("/teams/{teamId}/members/{memberId}")
    @Operation(summary = "그룹의 사용자 탈퇴")
    @Parameters(value = {
            @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다."),
            @Parameter(name = "memberId", description = "회원의 아이디, path variable 입니다.")
    })
    public ApiResponse deleteMemberTeam(@PathVariable Long teamId, @PathVariable Long memberId) {  // 그룹의 멤버 탈퇴 (본인 탈퇴는 가능. 멤버 탈퇴는 LEADER와 CREATOR만 가능. 리더 탈퇴는 CREATOR만 가능.)
        memberTeamService.deleteMemberTeam(teamId, memberId);
        return ApiResponse.onUpdateDelete(null);
    }
}
