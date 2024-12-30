package com.petmatz.domain.pet.component;

import com.petmatz.domain.pet.entity.Pet;
import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.pet.exception.PetErrorCode.PET_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PetReader {

    private final PetRepository petRepository;

    public List<Pet> getPetsByUserId(Long userId) {
        List<Pet> userPets = petRepository.findByUserId(userId);
        if (userPets.isEmpty()) {
            throw new PetServiceException(PET_NOT_FOUND);
        }
        return userPets;
    }

    public List<String> getTemperamentsByUserId(Long userId) {
        List<Pet> pets = petRepository.findByUserId(userId);
        if (pets.isEmpty()) {
            throw new RuntimeException();
        }
        return pets.stream()
                .map(pet -> pet.getTemperament() != null ? pet.getTemperament() : "Unknown")
                .collect(Collectors.toList());
    }

}
