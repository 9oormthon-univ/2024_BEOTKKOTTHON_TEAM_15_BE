package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.web.dto.response.ParticipationResponseDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationResponseDto> findParticipationByTeam(Long teamId);
}
