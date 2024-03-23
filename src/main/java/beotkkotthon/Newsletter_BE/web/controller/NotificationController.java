package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.MemberService;
import beotkkotthon.Newsletter_BE.service.NotificationService;
import beotkkotthon.Newsletter_BE.web.dto.request.FcmTokenRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.NotificationAllowRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Notification")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;


    @PostMapping("/notification")
    @Operation(summary = "로그인 성공 후 바로 FCM토큰 전달 [jwt O]")
    public ApiResponse saveNotification(@RequestBody FcmTokenRequestDto fcmTokenRequestDto) {
        notificationService.saveNotification(fcmTokenRequestDto);  // 로그인 직후 바로, fcm토큰 DB에 저장.
        return ApiResponse.onCreate(null);
    }

    @PutMapping("/notification")
    @Operation(summary = "알림 ON or OFF [jwt O]")
    public ApiResponse allowNotification(@RequestBody NotificationAllowRequestDto notificationAllowRequestDto) {
        memberService.allowNotification(notificationAllowRequestDto);
        return ApiResponse.onUpdateDelete(null);
    }
}
