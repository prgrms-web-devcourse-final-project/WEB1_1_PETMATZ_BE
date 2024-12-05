package com.petmatz.domain.match.component;

import com.petmatz.domain.match.dto.response.BoundingBoxResponse;
import org.springframework.stereotype.Component;

@Component
public class BoundingBoxCalculator {

    public BoundingBoxResponse calculateBoundingBox(double latitude, double longitude, double rangeKm) {
        double latChange = rangeKm / 111.0;  // 1도 = 111km
        double lngChange = rangeKm / (111.0 * Math.cos(Math.toRadians(latitude)));

        double minLat = latitude - latChange;
        double maxLat = latitude + latChange;
        double minLng = longitude - lngChange;
        double maxLng = longitude + lngChange;

        return new BoundingBoxResponse(minLat, maxLat, minLng, maxLng);
    }
}

