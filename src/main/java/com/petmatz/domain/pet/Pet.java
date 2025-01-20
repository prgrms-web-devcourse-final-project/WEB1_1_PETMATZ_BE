package com.petmatz.domain.pet;

import com.petmatz.domain.global.BaseEntity;
import com.petmatz.domain.pet.dto.PetServiceDto;
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

    public void addUser() {
        user = null;
    }

    public static Pet createFromDto(User user, PetServiceDto dto, String profileImg) {
        return Pet.builder()
                .user(user)
                .dogRegNo(dto.dogRegNo())
                .petName(dto.petName())
                .breed(dto.breed())
                .gender(Gender.fromString(dto.gender()))
                .neuterYn(dto.neuterYn())
                .size(Size.fromString(dto.size()))
                .age(dto.age())
                .temperament(dto.temperament())
                .preferredWalkingLocation(dto.preferredWalkingLocation())
                .profileImg(profileImg)
                .comment(dto.comment())
                .build();
    }

    public void updateFromDto(PetServiceDto dto, String profileImg) {
        this.petName = dto.petName() != null ? dto.petName() : this.petName;
        this.breed = dto.breed() != null ? dto.breed() : this.breed;
        this.gender = dto.gender() != null ? Gender.fromString(dto.gender()) : this.gender;
        this.neuterYn = dto.neuterYn() != null ? dto.neuterYn() : this.neuterYn;
        this.size = dto.size() != null ? Size.fromString(dto.size()) : this.size;
        this.age = dto.age() != null ? dto.age() : this.age;
        this.temperament = dto.temperament() != null ? dto.temperament() : this.temperament;
        this.preferredWalkingLocation = dto.preferredWalkingLocation() != null ? dto.preferredWalkingLocation() : this.preferredWalkingLocation;
        this.profileImg = profileImg != null ? profileImg : this.profileImg;
        this.comment = dto.comment() != null ? dto.comment() : this.comment;
    }
}



