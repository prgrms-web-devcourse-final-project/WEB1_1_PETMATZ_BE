package com.petmatz.domain.petmission.entity;

import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetMissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시작일시
    private LocalDateTime petMissionStarted;

    // 종료일시
    private LocalDateTime petMissionEnd;

    // 부탁 메시지
    private String comment;

    // 돌봄 상태 (true: 진행 중, false: 종료)
    private Boolean status;

    // 맡김이
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id", nullable = false)
    private User giver;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_mission_id")
    private List<PetMissionAskEntity> petMissionAsks = new ArrayList<>();

    // 미션 답변 (단일)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_mission_answer_id")
    private PetMissionAnswerEntity missionAnswer;


}
