package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.Authority;
import beotkkotthon.Newsletter_BE.domain.enums.NoticeStatus;
import beotkkotthon.Newsletter_BE.domain.mapping.MemberTeam;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor

@Table(name = "member")
@Entity
public class Member extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String username;

    @Enumerated(EnumType.STRING)
    private Authority authority;  // 초기값: ROLE_USER

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;  // 초기값: __null__

    @Column(name = "notice_status")
    @Enumerated(EnumType.STRING)
    private NoticeStatus noticeStatus;  // 초기값: ALLOW

    // (읽기 전용 필드) mappedBy만 사용으로, 조회 용도로만 가능. JPA는 insert나 update할 때 읽기 전용 필드를 아예 보지 않아서, 값을 넣어도 아무일도 일어나지않음.
    @OneToMany(mappedBy = "member")  // Member-MemberTeam 양방향매핑
    private List<MemberTeam> memberTeamList = new ArrayList<>();

    // (읽기 전용 필드)
    @OneToMany(mappedBy = "member")  // Member-News 양방향매핑
    private List<News> newsList = new ArrayList<>();


    @Builder(builderClassName = "MemberJoinBuilder", builderMethodName = "MemberJoinBuilder")
    public Member(String email, String password, String username, Authority authority, String imageUrl, NoticeStatus noticeStatus) {
        // 이 빌더는 사용자 회원가입때만 사용할 용도
        this.email = email;
        this.password = password;
        this.username = username;
        this.authority = authority;
        this.imageUrl = imageUrl;
        this.noticeStatus = noticeStatus;
    }

    @Builder(builderClassName = "MemberUpdateNameBuilder", builderMethodName = "MemberUpdateNameBuilder")
    public Member(String username) {
        // 이 빌더는 사용자 이름 수정때만 사용할 용도
        this.username = username;
    }


    // 수정(업데이트) 기능
    public void updateUsername(String username) {  // 사용자이름 변경 기능
        this.username = username;
    }
}
