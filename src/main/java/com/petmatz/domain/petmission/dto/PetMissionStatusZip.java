package com.petmatz.domain.petmission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PetMissionStatusZip {

    //        돌봄시작 상태값 { 시작 = PLG, 종료 = END }
    PLG("시작"),
    END("종료");

    private final String statusName;

    public static PetMissionStatusZip fromDescription(String description) {
        for (PetMissionStatusZip status : values()) {
            if (status.getStatusName().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown description: " + description);
    }


}
