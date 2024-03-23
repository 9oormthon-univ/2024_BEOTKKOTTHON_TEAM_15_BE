package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.web.dto.request.NotificationAllowRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberListResponseDto;

public interface MemberService {

    Member findById(Long memberId);
    MemberListResponseDto findMembersByTeam(Long teamId);
    void allowNotification(NotificationAllowRequestDto notificationAllowRequestDto);
}
