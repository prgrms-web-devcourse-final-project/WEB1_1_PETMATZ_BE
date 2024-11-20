package com.petmatz.domain.match.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchSizeService {

    public double calculateDogSizeScore(List<String> preferredSizes, String targetSize) {
        int totalPreferred = preferredSizes.size();

        if (preferredSizes.contains(targetSize)) {
            if (totalPreferred == 1) {
                if (preferredSizes.contains("Small") || preferredSizes.contains("Large")) {
                    return 20.0;
                }
                return 18.0;
            }
            if (totalPreferred == 2) {
                return 16.0;
            }
            if (totalPreferred == 3) {
                return 20.0 * 0.7;
            }
        }
        return 0.0;
    }

}
