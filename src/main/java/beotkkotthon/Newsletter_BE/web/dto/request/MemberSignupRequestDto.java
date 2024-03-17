package beotkkotthon.Newsletter_BE.web.dto.request;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.enums.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class MemberSignupRequestDto {

    private String email;
    private String password;
    private String username;

    @Builder
    public MemberSignupRequestDto(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.MemberJoinBuilder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
