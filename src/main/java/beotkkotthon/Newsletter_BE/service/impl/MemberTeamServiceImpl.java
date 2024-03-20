package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.MemberTeamRepository;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.service.MemberTeamService;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberTeamServiceImpl implements MemberTeamService {

    private final MemberTeamRepository memberTeamRepository;
    private final TeamService teamService;
    private final MemberService memberService;


    @Override
    public MemberTeam findByMemberAndTeam(Member member, Team team) {
        return memberTeamRepository.findByMemberAndTeam(member, team).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBERTEAM_NOT_FOUND));
    }

    @Transactional
    @Override
    public void deleteMemberTeam(Long teamId, Long memberId) {  // 그룹의 멤버 탈퇴 (MEMBER 및 LEADER 본인 탈퇴는 가능. CREATOR 본인 탈퇴는 불가능. 멤버 탈퇴는 LEADER와 CREATOR만 가능. 리더 탈퇴는 CREATOR만 가능.)
        // 탈퇴하고자하는 사용자
        Team team = teamService.findById(teamId);
        Member member = memberService.findById(memberId);
        MemberTeam memberTeam = findByMemberAndTeam(member, team);
        Role outRole = memberTeam.getRole();  // 탈퇴시키고자 하는 사용자의 Role

        // 현재 로그인된 사용자
        Long loginMemberId = SecurityUtil.getCurrentMemberId();
        Member loginMember = memberService.findById(loginMemberId);
        MemberTeam loginMemberTeam = findByMemberAndTeam(loginMember, team);
        Role loginRole = loginMemberTeam.getRole();  // 로그인 사용자의 Role

        if(memberId == loginMemberId) {  // 본인 탈퇴인 경우
            if(outRole.equals(Role.CREATOR)) {
                throw new GeneralException(ErrorStatus.BAD_REQUEST, "그룹 탈퇴 ERROR - 본인 CREATOR 탈퇴는 불가능");
            }
            else {
                team.teamSizeDown();  // 인원수 체킹 후 delete를 위해, 이 코드먼저 실행.
                memberTeamRepository.delete(memberTeam);  // success - MEMBER 및 LEADER 본인 탈퇴는 가능.
            }
        }
        else {  // 타인 퇴출일 경우
            if(loginRole.equals(Role.CREATOR)) {
                team.teamSizeDown();  // 인원수 체킹 후 delete를 위해, 이 코드먼저 실행.
                memberTeamRepository.delete(memberTeam);  // success - 본인이 CREATOR라면, 타인 전부 퇴출 가능.
            }
            else if(loginRole.equals(Role.LEADER)) {
                if(outRole.equals(Role.LEADER) || outRole.equals(Role.CREATOR)) {
                    throw new GeneralException(ErrorStatus.BAD_REQUEST, "그룹 탈퇴 권한 ERROR - LEADER는 LEADER or CREATOR인 타인 퇴출 불가능");
                }
                else {
                    team.teamSizeDown();  // 인원수 체킹 후 delete를 위해, 이 코드먼저 실행.
                    memberTeamRepository.delete(memberTeam);  // success - 본인이 LEADER라면, MEMBER 퇴출 가능.
                }
            }
            else {
                throw new GeneralException(ErrorStatus.BAD_REQUEST, "그룹 탈퇴 권한 ERROR - MEMBER는 타인 퇴출 불가능");
            }
        }
    }
}
