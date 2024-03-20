package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.MemberTeamService;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class MemberTeamController {

    private final MemberTeamService memberTeamService;


    @DeleteMapping("/teams/{teamId}/members/{memberId}")
    public ApiResponse deleteMemberTeam(@PathVariable Long teamId, @PathVariable Long memberId) {  // 그룹의 멤버 탈퇴 (본인 탈퇴는 가능. 멤버 탈퇴는 LEADER와 CREATOR만 가능. 리더 탈퇴는 CREATOR만 가능.)
        memberTeamService.deleteMemberTeam(teamId, memberId);
        return ApiResponse.onUpdateDelete(null);
    }
}
