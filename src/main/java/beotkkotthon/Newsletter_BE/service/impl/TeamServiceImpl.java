package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.TeamRepository;
import beotkkotthon.Newsletter_BE.service.TeamService;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team findById(Long id) {
        return teamRepository.findById(id).orElseThrow(
                () -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
    }
}
