package beotkkotthon.Newsletter_BE.web.dto.request;

import beotkkotthon.Newsletter_BE.domain.enums.NoticeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationAllowRequestDto {
    private NoticeStatus noticeStatus;
}
