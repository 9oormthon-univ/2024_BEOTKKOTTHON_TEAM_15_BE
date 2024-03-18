package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;



}
