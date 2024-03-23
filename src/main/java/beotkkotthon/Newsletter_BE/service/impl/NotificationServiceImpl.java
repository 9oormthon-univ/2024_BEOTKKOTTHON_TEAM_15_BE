package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.config.security.util.SecurityUtil;
import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Notification;
import beotkkotthon.Newsletter_BE.domain.enums.NoticeStatus;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.MemberRepository;
import beotkkotthon.Newsletter_BE.repository.NotificationRepository;
import beotkkotthon.Newsletter_BE.service.NotificationService;
import beotkkotthon.Newsletter_BE.web.dto.request.FcmTokenRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.NotificationDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void saveNotification(FcmTokenRequestDto fcmTokenRequestDto) {  // 로그인 직후 바로, fcm토큰 DB에 저장.
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // fcm토큰이 이미 존재할시, 덮어써서 재저장. => 사용자당 각각 가장 최근 로그인한 기기 1개로만 알림전송됨.
        if(notificationRepository.existsByMember(member)) {
            Notification beforeNotification = notificationRepository.findByMember(member)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NOTIFICATION_NOT_FOUND));
            notificationRepository.delete(beforeNotification);
        }

        Notification notification = Notification.builder()
                .token(fcmTokenRequestDto.getToken())
                .build();
        notification.confirmUser(member);

        notificationRepository.save(notification);
    }

    // @Transactional
    @Override
    public String getNotificationToken() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Notification notification = notificationRepository.findByMember(member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        return notification.getToken();
    }

    // @Transactional
    @Override
    public void sendNotification(Long memberId, NotificationDto notificationDto) throws ExecutionException, InterruptedException {

        // 로그인사용자가 계정에 알림설정을 꺼두었다면, 알림 전달 막기. (= 알림설정 켜둔경우에만 알림 응답 가능.)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if(member.getNoticeStatus().equals(NoticeStatus.ALLOW)) {
            Message message = Message.builder()
                    .setWebpushConfig(WebpushConfig.builder()
                            .setNotification(WebpushNotification.builder()
                                    .setTitle(notificationDto.getTitle())
                                    .setBody(notificationDto.getMessage())
                                    .setImage(notificationDto.getImage())
                                    .build())
                            .build())
                    .setToken(notificationDto.getToken())  // 수신할 기기의 토큰을 설정
                    .build();
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            log.info("Send 성공 : " + response);
        }
        else {
            log.info("Send 불가능 : " + "알림설정 OFF 상태입니다.");
        }
    }

    @Transactional
    @Override
    public void deleteNotification() {  // 로그아웃 시, fcm토큰 DB에서 삭제.
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Notification notification = notificationRepository.findByMember(member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }

    @Override
    public Optional<NotificationDto> makeMessage(Long memberId, String title, String message) {
        if(memberRepository.existsById(memberId)) {  // 해당 사용자가 존재할때
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

            if (member.getNotification() != null) {  // 해당 사용자가 fcm 기기등록이 되어있을때
                Notification notification = member.getNotification();

                NotificationDto notificationDto = NotificationDto.builder()
                        .title(title)
                        .token(notification.getToken())
                        .message(message)
                        .image("https://goormnotification.vercel.app/img/fcmLogo2.png")
                        .build();
                return Optional.of(notificationDto);
            }
        }

        return Optional.empty();
    }
}