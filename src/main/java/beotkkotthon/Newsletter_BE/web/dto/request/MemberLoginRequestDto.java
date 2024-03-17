package beotkkotthon.Newsletter_BE.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@NoArgsConstructor
public class MemberLoginRequestDto {

    private String email;
    private String password;

    @Builder
    public MemberLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    // UsernamePasswordAuthenticationToken을 반환하여 차후 이 객체를 이용하여 로그인계정아이디와 비밀번호가 일치하는지 검증하는 로직을 사용할 예정임.
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}