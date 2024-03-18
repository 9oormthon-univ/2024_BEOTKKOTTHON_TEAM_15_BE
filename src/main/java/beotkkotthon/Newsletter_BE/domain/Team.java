package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.Authority;
import beotkkotthon.Newsletter_BE.domain.enums.NoticeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor

@Table(name = "team")
@Entity
public class Team extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "team_size", columnDefinition = "INT default 1")
    private Integer teamSize;  // 초기값: 그룹장 포함 1명. 차후 최대 300명 제한시킬것.

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;  // 초기값: __null__

    @Column(length = 200)
    private String link;

    // mappedBy만 사용으로, 조회 용도로만 가능. (읽기 전용 필드)
    // JPA는 insert나 update할 때 읽기 전용 필드를 아예 보지 않아서, 값을 넣어도 아무일도 일어나지않음.
    @OneToMany(mappedBy = "team")
    private List<News> newsList = new ArrayList<>();
}
