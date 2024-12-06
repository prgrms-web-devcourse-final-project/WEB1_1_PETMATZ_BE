package com.petmatz.api.petmission.dto;

import com.petmatz.domain.petmission.dto.PetMissionCommentInfo;
import lombok.Builder;

@Builder
public record PetMissionCommentRequest(

        String missionId,
        String comment,
        String imgURL

) {
    public PetMissionCommentInfo of() {
        return PetMissionCommentInfo.builder()
                .missionId(missionId)
                .comment(comment)
                .imgURL(imgURL)
                .build();
    }
}
