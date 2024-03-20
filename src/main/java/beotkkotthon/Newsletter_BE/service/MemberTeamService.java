package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;

public interface MemberTeamService {
    MemberTeam findByMemberAndTeam(Member member, Team team);
    void deleteMemberTeam(Long teamId, Long memberId);  // 그룹의 멤버 탈퇴 (본인 탈퇴는 가능. 멤버 탈퇴는 LEADER와 CREATOR만 가능. 리더 탈퇴는 CREATOR만 가능.)
}
