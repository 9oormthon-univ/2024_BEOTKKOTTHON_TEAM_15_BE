package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto.ShowNewsDto;
import beotkkotthon.Newsletter_BE.web.dto.response.TeamResponseDto;

import java.util.List;

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

    public static TeamResponseDto.ShowTeamDto toShowTeamDto(Team team, Role role, List<ShowNewsDto> newsList, int leaderCount, int memberCount) {

        return TeamResponseDto.ShowTeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .teamSize(team.getTeamSize())
                .imageUrl(team.getImageUrl())
                .link(team.getLink())
                .role(role)
                .leaderCount(leaderCount)
                .memberCount(memberCount)
                .showNewsDtoList(newsList)
                .build();
    }
}
