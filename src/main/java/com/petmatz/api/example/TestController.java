package com.petmatz.api.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {


    @GetMapping("/chat1")
    public String chatPage() {
        return "chat";
    }
}
