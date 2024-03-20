package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.Member;

import java.util.Optional;

public interface MemberService {

    Member findById(Long memberId);
    MemberListResponseDto findMembersByTeam(Long teamId);
}
