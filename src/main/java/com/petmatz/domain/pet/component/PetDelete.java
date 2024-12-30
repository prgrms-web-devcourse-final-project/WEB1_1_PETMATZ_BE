package com.petmatz.domain.pet.component;

import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.pet.repository.PetRepository;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.petmatz.domain.pet.exception.PetErrorCode.PET_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PetDelete {

    private final PetRepository perRepository;

    public void deletePet(Long petId, User user) {
        perRepository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PET_NOT_FOUND));
        perRepository.deleteById(petId);
//        repository.delete(pet);
    }

}
