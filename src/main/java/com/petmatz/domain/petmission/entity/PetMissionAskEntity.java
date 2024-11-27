package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.petmission.dto.PetMissionAskInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetMissionAskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String comment;

    private String imgURL;

    @Builder
    public PetMissionAskEntity(String title, String comment, String imgURL) {
        this.title = title;
        this.comment = comment;
        this.imgURL = imgURL;
    }

    public static PetMissionAskEntity of(PetMissionAskInfo petMissionAskInfos) {
        return PetMissionAskEntity.builder()
                .title(petMissionAskInfos.title())
                .comment(petMissionAskInfos.comment())
                .imgURL(petMissionAskInfos.imgURL())
                .build();
    };


}
