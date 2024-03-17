package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.config.security.jwt.TokenDto;
import beotkkotthon.Newsletter_BE.config.security.jwt.TokenProvider;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.MemberRepository;
import beotkkotthon.Newsletter_BE.service.AuthService;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberLoginRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberSignupRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    @Override
    public MemberResponseDto signup(MemberSignupRequestDto memberSignupRequestDto) {

        String newEmail = memberSignupRequestDto.getEmail();
        memberRepository.findByEmail(newEmail)
                .ifPresent(member -> {  // 해당 로그인아이디의 사용자가 이미 존재한다면,
                    String errorMessage = ("중복된 아이디 '" + newEmail + "'를 입력했습니다.");
                    throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS, errorMessage);
                });

        Member entity = memberRepository.save(memberSignupRequestDto.toEntity(passwordEncoder));
        return new MemberResponseDto(entity);
    }

    @Transactional
    @Override
    public TokenDto login(MemberLoginRequestDto memberLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = memberLoginRequestDto.toAuthentication();

        // 여기서 실제로 검증이 이루어진다.
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }
}
