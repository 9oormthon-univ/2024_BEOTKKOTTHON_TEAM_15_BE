package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.repository.MemberTeamRepository;
import beotkkotthon.Newsletter_BE.service.MemberTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberTeamServiceImpl implements MemberTeamService {

    private final MemberTeamRepository memberTeamRepository;



}
