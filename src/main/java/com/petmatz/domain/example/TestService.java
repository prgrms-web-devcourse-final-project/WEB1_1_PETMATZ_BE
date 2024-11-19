package com.petmatz.domain.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestOpenApiInterface testOpenApiInterface;
//    private final TestReader testReader;

    public void testGet(TestDTO testDTO) {
        testOpenApiInterface.getTest();
//        throw new DomainException(GlobalErrorCode.PERMISSION_DENIED);
//        testReader.getTest();
    }
}
