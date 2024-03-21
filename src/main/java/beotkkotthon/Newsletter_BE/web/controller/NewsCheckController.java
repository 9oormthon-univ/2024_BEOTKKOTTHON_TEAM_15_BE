package beotkkotthon.Newsletter_BE.web.controller;

import beotkkotthon.Newsletter_BE.service.NewsCheckService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@Tag(name = "NewsCheck")
@RestController
@RequiredArgsConstructor
public class NewsCheckController {

    private final NewsCheckService newsCheckService;



}
