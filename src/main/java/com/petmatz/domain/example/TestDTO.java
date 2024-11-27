package com.petmatz.domain.example;

import lombok.Builder;
import lombok.Data;


@Builder
public record TestDTO(

        String name,
        String age

) {

}
