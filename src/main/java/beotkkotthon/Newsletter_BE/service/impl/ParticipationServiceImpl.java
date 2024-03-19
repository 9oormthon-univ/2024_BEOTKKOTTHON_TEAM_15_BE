package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.repository.ParticipationRepository;
import beotkkotthon.Newsletter_BE.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;



}
