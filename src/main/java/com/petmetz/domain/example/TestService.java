package com.petmetz.domain.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.basic.common.exception.DomainException;
import spring.basic.common.exception.GlobalErrorCode;
import spring.basic.common.exception.WebException;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestOpenApiInterface testOpenApiInterface;
//    private final TestReader testReader;

    public void testGet() {
        testOpenApiInterface.getTest();
//        throw new DomainException(GlobalErrorCode.PERMISSION_DENIED);
//        testReader.getTest();
    }
}
