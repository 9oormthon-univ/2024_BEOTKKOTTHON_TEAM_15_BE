package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.web.dto.request.TeamSaveRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.TeamResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamService {
    Team findById(Long id);

    TeamResponseDto createTeam(TeamSaveRequestDto teamSaveRequestDto) throws IOException;
    List<TeamResponseDto> findTeamsByMember(String name, String link);
}
