package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.web.dto.request.FcmTokenRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NotificationDto;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface NotificationService {
    void saveNotification(FcmTokenRequestDto fcmTokenRequestDto);  // 로그인 직후 바로, fcm토큰 DB에 저장.
    String getNotificationToken();
    void sendNotification(NotificationDto notificationDto) throws ExecutionException, InterruptedException;
    void deleteNotification();  // 로그아웃 시, fcm토큰 DB에서 삭제.

    Optional<NotificationDto> makeMessage(Long memberId, String title, String message);
}
