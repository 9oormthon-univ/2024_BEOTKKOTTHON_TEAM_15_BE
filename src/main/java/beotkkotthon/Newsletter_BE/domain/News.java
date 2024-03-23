package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "news")
@Entity
public class News extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "limit_time")
    private LocalDateTime limitTime;

    @Column(name = "image_url1", columnDefinition = "TEXT")
    private String imageUrl1; // 초기값: __null__

    @Column(name = "image_url2", columnDefinition = "TEXT")
    private String imageUrl2; // 초기값: __null__

    @ManyToOne(fetch = FetchType.LAZY)  // Team-News 양방향매핑
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)  // Member-News 양방향매핑
    @JoinColumn(name = "member_id")
    private Member member;

    // (읽기 전용 필드) mappedBy만 사용으로, 조회 용도로만 가능. JPA는 insert나 update할 때 읽기 전용 필드를 아예 보지 않아서, 값을 넣어도 아무일도 일어나지않음.
    @OneToMany(mappedBy = "news")  // News-NewsCheck 양방향매핑
    private List<NewsCheck> newsCheckList = new ArrayList<>();


    @Builder(builderClassName = "NewsSaveBuilder", builderMethodName = "NewsSaveBuilder")
    public News(String title, String content, Integer minute, String imageUrl1, String imageUrl2, Member member, Team team) {
        // 이 빌더는 가정통신문 생성때만 사용할 용도
        this.title = title;
        this.content = content;

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime resultTime = currentTime.plusMinutes(minute);
        this.limitTime = resultTime;

        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;

        this.member = member;
        this.team = team;
        // changeTeam(team);
    }

//    public void setTeam(Team team) {
//        this.team = team;
//        team.getNewsList().add(this);
//    }
//
//    public void addNewsCheck(NewsCheck newsCheck) {
//        newsCheckList
//    }
}
