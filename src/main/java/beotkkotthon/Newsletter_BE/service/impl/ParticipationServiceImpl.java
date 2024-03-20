package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.mapping.Participation;
import beotkkotthon.Newsletter_BE.repository.ParticipationRepository;
import beotkkotthon.Newsletter_BE.service.ParticipationService;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.ParticipationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final TeamService teamService;


    @Transactional(readOnly = true)
    @Override
    public List<ParticipationResponseDto> findParticipationByTeam(Long teamId) {
        Team team = teamService.findById(teamId);
        List<Participation> participationList = participationRepository.findAllByTeam(team);

        List<ParticipationResponseDto> participationResponseDtoList = new ArrayList<>();
        for(int i=0; i<participationList.size(); i++) {
            Participation participation = participationList.get(i);
            Member member = participation.getMember();
            ParticipationResponseDto participationResponseDto = new ParticipationResponseDto(
                    participation.getRequestRole(), participation.getCreatedTime(),
                    member.getId(), member.getEmail(), member.getUsername()
            );
            participationResponseDtoList.add(participationResponseDto);
        }

        participationResponseDtoList.sort(Comparator.comparing(ParticipationResponseDto::getRequestCreatedTime));  // 팀참여 신청 날짜최신순 정렬
        return participationResponseDtoList;
    }
}
