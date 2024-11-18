package com.petmetz.api.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.basic.api.global.dto.Response;
import spring.basic.domain.test.TestService;

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
