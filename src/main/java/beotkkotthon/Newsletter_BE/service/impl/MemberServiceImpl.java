package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.MemberRepository;
import beotkkotthon.Newsletter_BE.repository.TeamRepository;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.web.dto.request.NotificationAllowRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
//    private final TeamService teamService;  // 순환참조 문제로 주석처리. 대신 TeamRespository 이용하기.


    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public MemberListResponseDto findMembersByTeam(Long teamId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        List<MemberResponseDto> leaderList = new ArrayList<>();
        List<MemberResponseDto> memberList = new ArrayList<>();
        Integer leaderCount = 0, memberCount = 0;  // 주의: 본인을 제외한 숫자 카운팅임.
        Role myRole = null;

        List<MemberTeam> memberTeamList = team.getMemberTeamList();
        for (int i = 0; i < memberTeamList.size(); i++) {
            Role role = memberTeamList.get(i).getRole();
            if (memberTeamList.get(i).getMember().getId() != memberId) {  // 본인이 아닐때
                Member member = memberTeamList.get(i).getMember();
                if (role.name().equals("MEMBER")) {
                    memberList.add(new MemberResponseDto(member));
                    memberCount++;
                } else {  // LEADER 혹은 CREATOR 일때 (CREATOR도 LEADER 이다.)
                    leaderList.add(new MemberResponseDto(member));
                    leaderCount++;
                }
            } else myRole = role;
        }

        // 이 정렬방식은 연속sorted 정렬과는 다르게, 이름이 같을 경우에만 id 정렬을 해준다.
        leaderList.sort(Comparator.comparing(MemberResponseDto::getUsername)
                .thenComparing(MemberResponseDto::getId, Comparator.reverseOrder()));
        memberList.sort(Comparator.comparing(MemberResponseDto::getUsername)
                .thenComparing(MemberResponseDto::getId, Comparator.reverseOrder()));

        return new MemberListResponseDto(leaderList, memberList, leaderCount, memberCount, myRole);
    }

    @Transactional
    @Override
    public void allowNotification(@RequestBody NotificationAllowRequestDto notificationAllowRequestDto) {
        Member member = findById(SecurityUtil.getCurrentMemberId());
        member.updateNoticeStatus(notificationAllowRequestDto.getNoticeStatus());
    }
}
