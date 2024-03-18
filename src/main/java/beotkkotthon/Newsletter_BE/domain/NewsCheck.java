package beotkkotthon.Newsletter_BE.domain;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.Authority;
import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import beotkkotthon.Newsletter_BE.domain.enums.NoticeStatus;
import jakarta.persistence.*;
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
}
