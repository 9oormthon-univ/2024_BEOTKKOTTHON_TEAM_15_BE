package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@Tag(name = "Member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/teams/{teamId}/members")
    @Operation(summary = "본인을 제외한 그룹 멤버&리더 리스트 조회 [jwt O]")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<MemberListResponseDto> findMembersByTeam(@PathVariable Long teamId) {
        MemberListResponseDto memberListResponseDto = memberService.findMembersByTeam(teamId);
        return ApiResponse.onSuccess(memberListResponseDto);
    }
}
