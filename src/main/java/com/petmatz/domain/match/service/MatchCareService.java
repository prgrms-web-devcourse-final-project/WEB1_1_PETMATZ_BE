package com.petmatz.domain.match.service;


import com.petmatz.common.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
public class MatchCareService {

    public double calculateCareScore(Boolean isCareAvailable) {
        if (isCareAvailable == null) {
            return 10.0; // 추후 예외 처리
        }
        if (isCareAvailable) {
            return 20.0;
        } else {
            return 10.0;
        }
    }
}
