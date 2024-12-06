package com.petmatz.api.petmission.dto;

import com.petmatz.domain.petmission.dto.PetMissionCommentInfo;
import lombok.Builder;

@Builder
public record PetMissionCommentRequest(

        String askId,
        String comment,
        String imgURL

) {
    public PetMissionCommentInfo of() {
        return PetMissionCommentInfo.builder()
                .askId(askId)
                .comment(comment)
                .imgURL(imgURL)
                .build();
    }
}
