package beotkkotthon.Newsletter_BE.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenRequestDto {  //  (참고로 이 dto는 클라이언트가 보내준거임.)
    private String token;
}
