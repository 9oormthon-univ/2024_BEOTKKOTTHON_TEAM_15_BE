package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class NewsCheckController {

    private final NewsCheckService newsCheckService;



}
