package beotkkotthon.Newsletter_BE.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationDto {  // notificationService.sendNotification()의 파라미터로 쓰일 예정. (참고로 이 dto는 클라이언트가 보내준게 아님.)

    private String title;
    private String message;
    private String token;

    @Builder
    public NotificationDto(String title, String message, String token) {
        this.title = title;
        this.message = message;
        this.token = token;
    }
}