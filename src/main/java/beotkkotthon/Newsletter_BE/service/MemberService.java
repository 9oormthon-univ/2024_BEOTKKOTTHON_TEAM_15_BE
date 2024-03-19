package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;

public interface MemberService {
    Member findById(Long id);

    MemberListResponseDto findMembersByTeam(Long teamId);
}
