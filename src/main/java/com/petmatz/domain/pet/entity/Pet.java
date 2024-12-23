package com.petmatz.domain.pet.entity;

import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.pet.Gender;
import com.petmatz.domain.pet.Size;
import com.petmatz.domain.pet.vo.PetInf;
import com.petmatz.domain.pet.vo.PetUpdateInfo;
import com.petmatz.domain.petmission.entity.PetToPetMissionEntity;
import com.petmatz.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetToPetMissionEntity> petToPetMissions = new ArrayList<>();

    public boolean checkImgURL(String Img) {
        return profileImg.equals(Img);
    }

    public void addUser(User userData) {
        user = userData;
    }

    public static Pet of(PetInf petInf, String imgURL, User user) {
        return Pet.builder()
                .user(user)
                .dogRegNo(petInf.dogRegNo())
                .petName(petInf.petName())
                .breed(petInf.breed())
                .gender(Gender.fromString(petInf.gender()))
                .neuterYn(petInf.neuterYn())
                .size(Size.fromString(petInf.size()))
                .age(petInf.age())
                .temperament(petInf.temperament())
                .preferredWalkingLocation(petInf.preferredWalkingLocation())
                .profileImg(imgURL)
                .comment(petInf.comment())
                .build();
    }

    public void updatePetInfo(PetUpdateInfo petUpdateInfo, String imgUrl) {
        age = petUpdateInfo.age();
        breed = petUpdateInfo.breed();
        comment = petUpdateInfo.comment();
        gender = petUpdateInfo.gender();
        neuterYn = petUpdateInfo.neuterYn();
        petName = petUpdateInfo.petName();
        profileImg = imgUrl;
        size = petUpdateInfo.size();
        temperament = petUpdateInfo.temperament();
    }

}

