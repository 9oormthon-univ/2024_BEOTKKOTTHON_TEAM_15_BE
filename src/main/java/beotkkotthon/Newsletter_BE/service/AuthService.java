package beotkkotthon.Newsletter_BE.service;

import beotkkotthon.Newsletter_BE.config.security.jwt.TokenDto;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberLoginRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.request.MemberSignupRequestDto;
import beotkkotthon.Newsletter_BE.web.dto.response.MemberResponseDto;

public interface AuthService {

    MemberResponseDto signup(MemberSignupRequestDto memberSignupRequestDto);
    TokenDto login(MemberLoginRequestDto memberLoginRequestDto);
}
