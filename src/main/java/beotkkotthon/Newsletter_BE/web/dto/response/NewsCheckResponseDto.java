package beotkkotthon.Newsletter_BE.web.dto.response;

import beotkkotthon.Newsletter_BE.domain.enums.CheckStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NewsCheckResponseDto {

    private CheckStatus checkStatus;
    private Long checkTime;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsCheckDto {
        CheckStatus checkStatus;
        Long checkTime;
    }
}
