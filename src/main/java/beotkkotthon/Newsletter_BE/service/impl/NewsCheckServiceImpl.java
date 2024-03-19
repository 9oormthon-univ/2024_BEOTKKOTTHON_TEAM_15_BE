package beotkkotthon.Newsletter_BE.service.impl;

import beotkkotthon.Newsletter_BE.repository.NewsCheckRepository;
import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsCheckServiceImpl implements NewsCheckService {

    private final NewsCheckRepository newsCheckRepository;



}
