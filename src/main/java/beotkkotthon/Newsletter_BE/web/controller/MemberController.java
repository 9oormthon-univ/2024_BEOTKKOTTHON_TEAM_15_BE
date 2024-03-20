package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/teams/{teamId}/members")  // 본인을 제외하고 팀 멤버&리더 리스트 조회
    public ApiResponse<MemberListResponseDto> findMembersByTeam(@PathVariable Long teamId) {
        MemberListResponseDto memberListResponseDto = memberService.findMembersByTeam(teamId);
        return ApiResponse.onSuccess(memberListResponseDto);
    }
}
