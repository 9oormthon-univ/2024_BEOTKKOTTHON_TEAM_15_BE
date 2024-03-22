package beotkkotthon.Newsletter_BE.web.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {  // jwt Token ResponseDto (+ login username)

    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;

    @Setter
    private String username;
}