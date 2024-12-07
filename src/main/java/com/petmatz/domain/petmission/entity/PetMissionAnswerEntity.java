package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.petmission.dto.PetMissionAnswerInfo;
import com.petmatz.domain.petmission.dto.PetMissionCommentInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetMissionAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private String imgURL;

    @Builder
    public PetMissionAnswerEntity(String comment, String imgURL) {
        this.comment = comment;
        this.imgURL = imgURL;
    }

    public static PetMissionAnswerEntity of(PetMissionCommentInfo petMissionCommentInfo, String imgURL) {
        return PetMissionAnswerEntity.builder()
                .comment(petMissionCommentInfo.comment())
                .imgURL(imgURL)
                .build();
    }

    public PetMissionAnswerInfo of() {
        return PetMissionAnswerInfo.builder()
                .id(id)
                .comment(comment)
                .imgURL(imgURL)
                .build();
    }


    public String checkCommentNull() {
        return comment != null ? comment : "없음";
    }

    public String checkURLNull() {
        return imgURL != null ? imgURL : "없음";
    }

    public void updateAnswer(PetMissionCommentInfo petMissionCommentInfo, String imgURL) {
        this.comment = petMissionCommentInfo.comment();
        if (!imgURL.equals("")) {
            this.imgURL = imgURL;
        }
    }
}
