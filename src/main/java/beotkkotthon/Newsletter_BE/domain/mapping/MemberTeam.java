package beotkkotthon.Newsletter_BE.domain.mapping;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "member_team")
@Entity
public class MemberTeam extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)  // Member-MemberTeam 양방향매핑
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)  // Team-MemberTeam 양방향매핑
    @JoinColumn(name = "team_id")
    private Team team;


    @Builder(builderClassName = "MemberTeamSaveBuilder", builderMethodName = "MemberTeamSaveBuilder")
    public MemberTeam(Role role, Member member, Team team) {
        // 이 빌더는 그룹 생성때만 사용할 용도
        this.role = role;
        this.member = member;
        this.team = team;
    }
}
