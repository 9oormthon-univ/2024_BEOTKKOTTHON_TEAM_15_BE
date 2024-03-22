package beotkkotthon.Newsletter_BE.converter;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import beotkkotthon.Newsletter_BE.web.dto.response.NewsResponseDto;
import beotkkotthon.Newsletter_BE.web.dto.response.TeamResponseDto;

import java.util.List;
import java.util.stream.Collectors;

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

    public static TeamResponseDto.ShowTeamDto toShowTeamDto(Team team, Role role, List<News> newsList, int leaderCount, int memberCount) {
        List<NewsResponseDto.ShowNewsDto> showNewsDtoList = newsList.stream()
                .map(NewsConverter::toShowNewsDto)
                .collect(Collectors.toList());

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
                .showNewsDtoList(showNewsDtoList)
                .build();
    }
}
