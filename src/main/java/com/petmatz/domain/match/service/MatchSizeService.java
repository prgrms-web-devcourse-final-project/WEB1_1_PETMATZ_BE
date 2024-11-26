package com.petmatz.domain.match.service;

import com.petmatz.domain.match.exception.MatchException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_PREFERRED_SIZES;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_TARGET_SIZE;

@Service
public class MatchSizeService {

    public double calculateDogSizeScore(List<String> preferredSizes, String targetSize) {
        if (preferredSizes == null) {
            throw new MatchException(NULL_PREFERRED_SIZES);
        }
        if (targetSize == null) {
            throw new MatchException(NULL_TARGET_SIZE);
        }

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
