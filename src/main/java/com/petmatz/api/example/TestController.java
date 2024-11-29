package com.petmatz.api.example;

import com.petmatz.domain.example.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {


    @GetMapping("/chat1")
    public String chatPage() {
        return "chat";
    }
}
