package com.petmatz.api.example;

import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.example.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final TestService testService;

    @GetMapping
    public Response<?> testGet(@RequestParam String tt, @RequestBody TestDTO testDTO) {
        testService.testGet();
        return Response.success("test");
    }

}
