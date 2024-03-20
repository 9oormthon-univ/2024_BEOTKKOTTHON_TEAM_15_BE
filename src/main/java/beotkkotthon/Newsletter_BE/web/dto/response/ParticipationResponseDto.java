package beotkkotthon.Newsletter_BE.web.dto.response;

import beotkkotthon.Newsletter_BE.domain.enums.RequestRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationResponseDto {

    // Participation 정보 관련
    private RequestRole requestRole;
    private LocalDateTime requestCreatedTime;

    // Member 정보 관련
    private Long id;
    private String email;
    private String username;
}
