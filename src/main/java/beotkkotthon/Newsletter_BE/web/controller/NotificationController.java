package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.payload.ApiResponse;
import beotkkotthon.Newsletter_BE.service.NotificationService;
import beotkkotthon.Newsletter_BE.web.dto.request.FcmTokenRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Notification")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/notification")
    @Operation(summary = "로그인 성공 후 바로 FCM토큰 전달 [jwt O]")
    public ApiResponse saveNotification(@RequestBody FcmTokenRequestDto fcmTokenRequestDto) {
        notificationService.saveNotification(fcmTokenRequestDto);  // 로그인 직후 바로, fcm토큰 DB에 저장.
        return ApiResponse.onCreate(null);
    }
}
