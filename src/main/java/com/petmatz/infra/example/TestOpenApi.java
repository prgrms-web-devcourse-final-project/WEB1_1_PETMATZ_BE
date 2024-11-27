package com.petmatz.infra.example;

import com.petmatz.domain.example.TestOpenApiInterface;
import org.springframework.stereotype.Component;


@Component
public class TestOpenApi implements TestOpenApiInterface {
    @Override
    public void getTest() {

    }
}
