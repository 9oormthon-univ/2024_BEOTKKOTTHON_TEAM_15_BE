package beotkkotthon.Newsletter_BE.web.dto.response;

import beotkkotthon.Newsletter_BE.domain.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class NewsResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime limitTime;
    private String writer;
    private String teamName;
    private String imageUrl1;
    private String imageUrl2;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public NewsResponseDto(News entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.limitTime = entity.getLimitTime();
        this.writer = entity.getMember().getUsername();
        this.teamName = entity.getTeam().getName();
        this.imageUrl1 = entity.getImageUrl1();
        this.imageUrl2 = entity.getImageUrl2();
        this.createdTime = entity.getCreatedTime();
        this.modifiedTime = entity.getModifiedTime();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowNewsDto {
        Long newsId;
        String title;
        String content;
        String writer;
        String imageUrl1;
        String imageUrl2;
        LocalDateTime limitTime;
        int readMemberCount;
        int notReadMemberCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowNewsListDto {
        List<ShowNewsDto> showNewsDtoList;
        List<NewsCheckResponseDto.NewsCheckDto> newsCheckResponseDtoList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsDetailDto{
        ShowNewsDto showNewsDto;
        List<NewsCheckResponseDto.NewsCheckDto> newsCheckDtoList;
    }
}
