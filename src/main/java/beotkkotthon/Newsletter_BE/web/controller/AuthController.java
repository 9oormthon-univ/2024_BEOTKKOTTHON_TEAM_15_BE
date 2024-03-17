package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.config.security.jwt.TokenDto;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.AuthService;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberLoginRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberSignupRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ApiResponse<MemberResponseDto> signUp(@RequestBody MemberSignupRequestDto memberSignupRequestDto) {
        MemberResponseDto memberResponseDto = authService.signup(memberSignupRequestDto);
        return ApiResponse.onCreate(memberResponseDto);
    }

    @PostMapping("/login")
    public ApiResponse<TokenDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        TokenDto tokenDto = authService.login(memberLoginRequestDto);
        return ApiResponse.onSuccess(tokenDto);
    }
}
