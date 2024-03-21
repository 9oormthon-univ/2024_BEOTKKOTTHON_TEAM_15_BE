package beotkkotthon.Newsletter_BE.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {  // jwt Token ResponseDto

    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
}