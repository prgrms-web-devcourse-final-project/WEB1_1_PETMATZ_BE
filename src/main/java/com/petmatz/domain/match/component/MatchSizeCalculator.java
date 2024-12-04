package com.petmatz.domain.match.component;

import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.pet.PetServiceImpl;
import com.petmatz.domain.pet.exception.PetServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_PREFERRED_SIZES;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_TARGET_SIZE;
import static com.petmatz.domain.pet.exception.PetErrorCode.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MatchSizeCalculator {

    private final PetServiceImpl petService;
    public double calculateDogSizeScore(Long userId, List<String> targetPreferredSizes) {
        List<Pet> userPets = petService.getPetsByUserId(userId);

        if (targetPreferredSizes == null || targetPreferredSizes.isEmpty()) {
            throw new MatchException(NULL_PREFERRED_SIZES);
        }

        double totalScore = userPets.stream()
                .mapToDouble(pet -> {
                    String myPetSize = pet.getSize().name();
                    return calculateScoreForPet(targetPreferredSizes, myPetSize);
                })
                .sum();

        return totalScore / userPets.size();
    }


    private double calculateScoreForPet(List<String> targetPreferredSizes, String myPetSize) {
        if (myPetSize == null) {
            throw new MatchException(NULL_TARGET_SIZE);
        }
        List<String> normalizedTargetPreferredSizes = targetPreferredSizes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        String normalizedMyPetSize = myPetSize.toUpperCase();

        if (normalizedTargetPreferredSizes.contains(normalizedMyPetSize)) {
            int totalPreferred = normalizedTargetPreferredSizes.size();

            if (totalPreferred == 1) {
                if (normalizedTargetPreferredSizes.contains("SMALL") || normalizedTargetPreferredSizes.contains("LARGE")) {
                    return 20.0;
                }
                return 18.0;
            }
            if (totalPreferred == 2) {
                return 16.0;
            }
            if (totalPreferred == 3) {
                return 20.0 * 0.7; // 선호 크기가 3개일 경우 점수 감소
            }
        }
        return 6.0;
    }


}