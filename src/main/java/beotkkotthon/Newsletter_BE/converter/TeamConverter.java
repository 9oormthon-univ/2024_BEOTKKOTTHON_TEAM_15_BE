package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.web.dto.response.TeamResponseDto;

public class TeamConverter {

    public static TeamResponseDto.TeamDto toTeamResultDto(Team team) {
        return TeamResponseDto.TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .teamSize(team.getTeamSize())
                .imageUrl(team.getImageUrl())
                .link(team.getLink())
                .build();
    }
}
