package beotkkotthon.Newsletter_BE.web.dto.request;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.News;
import beotkkotthon.Newsletter_BE.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class NewsSaveRequestDto {

    private String title;
    private String content;
    private Integer minute;
    private MultipartFile image1;
    private MultipartFile image2;

    @Builder
    public NewsSaveRequestDto (String title, String content, Integer minute, MultipartFile image1, MultipartFile image2) {
        this.title = title;
        this.content = content;
        this.minute = minute;
        this.image1 = image1;
        this.image2 = image2;
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
