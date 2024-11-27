package com.petmatz.api.user.request;

import com.petmatz.domain.user.info.RepasswordInfo;
import com.petmatz.domain.user.info.UpdateLocationInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateLocationRequestDto {
    @NotBlank
    private Double latitude;

    @NotBlank
    private Double longitude;

    public static UpdateLocationInfo of(UpdateLocationRequestDto reqDto) {
        return UpdateLocationInfo.builder()
                .latitude(reqDto.getLatitude())
                .longitude(reqDto.getLongitude())
                .build();
    }
}
