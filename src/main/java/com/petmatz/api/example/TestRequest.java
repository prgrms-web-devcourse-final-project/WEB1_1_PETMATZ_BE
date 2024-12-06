package com.petmatz.api.example;

import com.petmatz.domain.example.TestDTO;
import lombok.Data;

public record TestRequest(
        String base64Image
) {


}
