package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.mapping.Participation;
import beotkkotthon.Newsletter_BE.web.dto.request.ParticipationRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.ParticipationResponseDto;

import java.util.List;

public interface ParticipationService {
    Participation findByMemberAndTeam(Member member, Team team);
    List<ParticipationResponseDto> findParticipationByTeam(Long teamId);
    void acceptParticipation(Long teamId, ParticipationRequestDto participationRequestDto);  // 새멤버 수락/거절 결과 Participation에서 해당 데이터 삭제 후, 수락일 경우 MemberTeam에도 추가.
}
