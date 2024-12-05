package com.petmatz.domain.match.dto.response;

public record BoundingBoxResponse(
        double minLat, double maxLat, double minLng, double maxLng
) {

}

