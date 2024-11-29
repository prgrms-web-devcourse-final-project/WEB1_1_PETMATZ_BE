package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.petmission.dto.PetMissionAskInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PetMissionAskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String comment;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_mission_answer_id")
    private PetMissionAnswerEntity missionAnswer;

    @Builder
    public PetMissionAskEntity(String title, String comment, String imgURL) {
        this.comment = comment;
    }

    public static PetMissionAskEntity of(String comment) {
        return PetMissionAskEntity.builder()
                .comment(comment)
                .build();
    };


}
