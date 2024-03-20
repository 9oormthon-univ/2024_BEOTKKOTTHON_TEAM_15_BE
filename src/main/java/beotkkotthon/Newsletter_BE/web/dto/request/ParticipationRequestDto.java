package beotkkotthon.Newsletter_BE.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipationRequestDto {

    private Long memberId;
    private Boolean isAccept;
}
