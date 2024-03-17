package beotkkotthon.Newsletter_BE.domain.mapping;

import beotkkotthon.Newsletter_BE.domain.common.BaseEntity;
import beotkkotthon.Newsletter_BE.domain.enums.RequestRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor

@Table(name = "participation")
@Entity
public class Participation extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_role")
    @Enumerated(EnumType.STRING)
    private RequestRole requestRole;
}
