package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;

public interface MemberService {
    MemberListResponseDto findMembersByTeam(Long teamId);
}
