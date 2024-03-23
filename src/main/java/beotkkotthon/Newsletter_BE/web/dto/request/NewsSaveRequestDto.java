package beotkkotthon.Newsletter_BE.web.dto.request;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsSaveRequestDto {

    private String title;
    private String content;
    private Integer minute;

    @Builder
    public NewsSaveRequestDto (String title, String content, Integer minute) {
        this.title = title;
        this.content = content;
        this.minute = minute;
    }

    public News toEntity(Member member, Team team, Integer minute, String imageUrl1, String imageUrl2) {
        return News.NewsSaveBuilder()
                .title(title)
                .content(content)
                .minute(minute)
                .imageUrl1(imageUrl1)
                .imageUrl2(imageUrl2)
                .member(member)
                .team(team)
                .build();
    }
}