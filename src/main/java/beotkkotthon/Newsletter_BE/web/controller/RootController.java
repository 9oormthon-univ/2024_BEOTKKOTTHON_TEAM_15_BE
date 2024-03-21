package beotkkotthon.Newsletter_BE.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@Tag(name = "Root")
public class RootController {

    @GetMapping("/health")
    @Operation(summary = "서버 헬스체크")
    public String healthCheck() {
        return "Fighting!!!️";
    }
}
