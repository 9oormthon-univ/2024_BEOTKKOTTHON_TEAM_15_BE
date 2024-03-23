package beotkkotthon.Newsletter_BE.web.dto.response;

import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class TeamResponseDto {

    private Long id;
    private String name;
    private String description;
    private Integer teamSize;
    private String imageUrl;
    private String link;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public TeamResponseDto(Team entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.teamSize = entity.getTeamSize();
        this.imageUrl = entity.getImageUrl();
        this.link = entity.getLink();
        this.createdTime = entity.getCreatedTime();
        this.modifiedTime = entity.getModifiedTime();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDto{
        private Long id;
        private String name;
        private String description;
        private Integer teamSize;
        private String imageUrl;
        private String link;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowTeamDto{
        private Long id;
        private String name;
        private String description;
        private Integer teamSize;
        private String imageUrl;
        private String link;
        private Role role;
        int leaderCount;
        int memberCount;
        private List<NewsResponseDto.ShowNewsDto> showNewsDtoList;
    }
}
