package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.domain.Member;
import beotkkotthon.Newsletter_BE.domain.Team;
import beotkkotthon.Newsletter_BE.payload.exception.GeneralException;
import beotkkotthon.Newsletter_BE.payload.status.ErrorStatus;
import beotkkotthon.Newsletter_BE.repository.MemberRepository;
import beotkkotthon.Newsletter_BE.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
