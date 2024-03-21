package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)  // Member-Notification 양방향매핑
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public Notification(String token) {
        this.token = token;
    }

    public void confirmUser(Member member) {
        this.member = member;
    }
}