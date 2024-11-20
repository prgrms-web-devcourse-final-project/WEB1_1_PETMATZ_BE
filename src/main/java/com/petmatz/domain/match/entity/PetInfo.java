package com.petmatz.domain.match.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


// 희수 : 철진님 임시 엔티티
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "Pet")
@Table(name = "Pet")
public class PetInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User 테이블 참조

    @Column(name = "pet_name", nullable = false)
    private String petName; // 개 이름

    @Column(name = "breed", nullable = false)
    private String breed; // 품종

    @Column(name = "age", nullable = false)
    private int age; // 나이

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender; // 성별

    @Column(name = "is_neutered", nullable = false)
    private String isNeutered; // 중성화 여부

    @Column(name = "temperament")
    private String temperament; // 성격

    @Column(name = "preferred_walking_location")
    private String preferredWalkingLocation; // 선호 산책 장소

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private Size size; // 크기

    @Column(name = "profile_img")
    private String profileImg; // 프로필 이미지 경로 또는 URL

    @Column(name = "comment")
    private String comment; // 코멘트

    public enum Gender {
        암컷, 수컷
    }

    public enum Size {
        Small, Medium, Large
    }
}