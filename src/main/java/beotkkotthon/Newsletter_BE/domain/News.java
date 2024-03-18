package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.Authority;
import beotkkotthon.Newsletter_BE.domain.enums.NoticeStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    @Builder(builderClassName = "NewsSaveBuilder", builderMethodName = "NewsSaveBuilder")
    public News(String title, String content, Integer minute, Team team) {
        // 이 빌더는 가정통신문 생성때만 사용할 용도
        this.title = title;
        this.content = content;

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime resultTime = currentTime.plusMinutes(minute);
        this.limitTime = resultTime;

        // this.team = team;
        changeTeam(team);
    }


    // News-Team 연관관계 편의 메소드
    public void changeTeam(Team team){
        this.team = team;
        team.getNewsList().add(this);
    }
}
