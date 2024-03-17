package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.Authority;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private Authority authority;


    @Builder
    public Member(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    @Builder(builderClassName = "MemberJoinBuilder", builderMethodName = "MemberJoinBuilder")
    public Member(String email, String password, String username, Authority authority) {
        // 이 빌더는 사용자 회원가입때만 사용할 용도
        this.email = email;
        this.password = password;
        this.username = username;
        this.authority = authority;
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
