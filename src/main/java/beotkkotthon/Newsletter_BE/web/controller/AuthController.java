package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.MemberRepository;
import beotkkotthon.Newsletter_BE.service.NotificationService;
import beotkkotthon.Newsletter_BE.web.dto.response.TokenDto;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.AuthService;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberLoginRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberSignupRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = "https://goormnotification.vercel.app", allowedHeaders = "*")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final NotificationService notificationService;

    private final MemberRepository memberRepository;


    @PostMapping("/signup")
    @Operation(summary = "회원가입 [jwt X]")
    public ApiResponse<MemberResponseDto> signUp(@RequestBody MemberSignupRequestDto memberSignupRequestDto) {
        MemberResponseDto memberResponseDto = authService.signup(memberSignupRequestDto);
        return ApiResponse.onCreate(memberResponseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 [jwt X]")
    public ApiResponse<TokenDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        TokenDto tokenDto = authService.login(memberLoginRequestDto);  // 로그인.

        Member member = memberRepository.findByEmail(memberLoginRequestDto.getEmail()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        tokenDto.setUsername(member.getUsername());
        tokenDto.setEmail(member.getEmail());

        return ApiResponse.onSuccess(tokenDto);
    }

    @GetMapping("/auth/logout")
    @Operation(summary = "로그아웃 [jwt O]")
    public ApiResponse logout() {  // 로그아웃 시, fcm토큰 DB에서 삭제.
        notificationService.deleteNotification();
        return ApiResponse.onSuccess(null);
    }
}
