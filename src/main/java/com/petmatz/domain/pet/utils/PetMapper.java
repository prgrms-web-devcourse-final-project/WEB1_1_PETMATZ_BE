package com.petmatz.domain.pet.utils;

import com.petmatz.domain.pet.Gender;
import com.petmatz.domain.pet.Size;
import com.petmatz.domain.pet.entity.Pet;
import com.petmatz.domain.pet.dto.PetInf;
import com.petmatz.domain.user.entity.User;

public class PetMapper {

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

}
