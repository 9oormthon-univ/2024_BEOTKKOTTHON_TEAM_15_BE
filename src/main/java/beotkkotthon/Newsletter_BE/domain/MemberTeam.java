package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.Authority;
import beotkkotthon.Newsletter_BE.domain.enums.RequestRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor

@Table(name = "member_team")
@Entity
public class MemberTeam extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_role")
    @Enumerated(EnumType.STRING)
    private RequestRole requestRole;  // 초기값: ROLE_USER
}
