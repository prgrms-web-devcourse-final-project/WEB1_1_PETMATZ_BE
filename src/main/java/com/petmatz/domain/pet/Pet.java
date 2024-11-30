package com.petmatz.domain.pet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "Pet")
@Table(name = "Pet")
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User 테이블 참조

    @Column(name = "pet_name", nullable = false)
    private String petName; // 개 이름

    @Column(name = "breed", nullable = false)
    private String breed; // 품종

    @Column(name = "age", nullable = false)
    private Integer age; // 나이

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender; // 성별

    @Getter
    @Column(name = "is_neutered", columnDefinition = "VARCHAR(10)", nullable = false)
    private String neuterYn; // 중성화 여부

    @Column(name = "temperament")
    private String temperament; // 성격

    @Column(name = "preferred_walking_location")
    private String preferredWalkingLocation; // 선호 산책 장소

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private Size size; // 크기

    @Column(name = "dog_reg_no", nullable = false)
    private String dogRegNo; // 동물등록번호

    @Column(name = "profile_img")
    private String profileImg; // 프로필 이미지 경로 또는 URL

    @Column(name = "comment")
    private String comment; // 코멘트
}

