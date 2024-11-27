package com.petmatz.api.example;

import com.petmatz.domain.example.TestDTO;
import lombok.Data;

public record TestRequest(
        String name,
        String age
) {


    TestDTO of() {
        return TestDTO.builder()
                .name(name)
                .age(age)
                .build();
    }
}
