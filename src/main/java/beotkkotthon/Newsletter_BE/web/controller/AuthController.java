package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.service.NotificationService;
import beotkkotthon.Newsletter_BE.web.dto.request.FcmTokenRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.TokenDto;
import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.AuthService;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberLoginRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberSignupRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final NotificationService notificationService;


    @PostMapping("/signup")
    public ApiResponse<MemberResponseDto> signUp(@RequestBody MemberSignupRequestDto memberSignupRequestDto) {
        MemberResponseDto memberResponseDto = authService.signup(memberSignupRequestDto);
        return ApiResponse.onCreate(memberResponseDto);
    }

    @PostMapping("/login")
    public ApiResponse<TokenDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        TokenDto tokenDto = authService.login(memberLoginRequestDto);  // 로그인.
        notificationService.saveNotification(new FcmTokenRequestDto(memberLoginRequestDto.getFcmToken()));  // 로그인 시, fcm토큰 DB에 저장.
        return ApiResponse.onSuccess(tokenDto);
    }

    @DeleteMapping("/logout")
    public ApiResponse logout() {  // 로그아웃 시, fcm토큰 DB에서 삭제.
        notificationService.deleteNotification();
        return ApiResponse.onUpdateDelete(null);
    }
}
