package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name = "news_check")
@Entity
public class NewsCheck extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_status")
    @Enumerated(EnumType.STRING)
    private CheckStatus checkStatus;  // 초기값: NOT_READ

    @Column(name = "check_time", columnDefinition = "BIGINT default 0")
    private Long checkTime;  // 초기값: 0

    @ManyToOne(fetch = FetchType.LAZY)  // Member-NewsCheck 양방향으로 변경 (Member에서 NewsCheck 미확인/확인 목록 조회에 사용) (자식 엔티티)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)  // News-NewsCheck 양방향매핑 (News에서 NewsCheck를 조회하기 때문.)
    @JoinColumn(name = "news_id")
    private News news;

    @Builder(builderClassName = "NewsCheckBuilder", builderMethodName = "NewsCheckCreateBuilder")
    public NewsCheck (Member member, News news, Long checkTime) {
        this.checkStatus = CheckStatus.NOT_READ;
        this.member = member;
        this.news = news;
        this.checkTime = checkTime;
    }

    public NewsCheck updateStatus(CheckStatus updatedStatus) {
        this.checkStatus = updatedStatus;
        return this;
    }
}
