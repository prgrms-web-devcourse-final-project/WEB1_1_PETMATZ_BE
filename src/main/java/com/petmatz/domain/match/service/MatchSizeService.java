package com.petmatz.domain.match.service;

import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_PREFERRED_SIZES;
import static com.petmatz.domain.match.exception.MatchErrorCode.NULL_TARGET_SIZE;
import static com.petmatz.domain.pet.exception.PetErrorCode.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MatchSizeService {

    private final PetRepository petRepository;

    // TODO 현재는 임시로 펫 직접 조회 추후에 펫 쪽에서 사이즈 구현 의뢰 예정
    public double calculateDogSizeScore(Long targetUserId, List<String> preferredSizes) {
        {
            List<Pet> userPets = petRepository.findByUserId(targetUserId);

            // 디버깅 로그 추가
            System.out.println("유저 ID: " + targetUserId);
            System.out.println("유저 펫 리스트: " + userPets);

            if (userPets.isEmpty()) {
                throw new PetServiceException(PET_NOT_FOUND);
            }

            if (preferredSizes == null || preferredSizes.isEmpty()) {
                throw new MatchException(NULL_PREFERRED_SIZES);
            }

            double totalScore = userPets.stream()
                    .mapToDouble(pet -> calculateScoreForPet(preferredSizes, pet.getSize().name()))
                    .sum();

            System.out.println("두두둥 : " + totalScore / userPets.size());
            return totalScore / userPets.size();

        }
    }

    private double calculateScoreForPet(List<String> preferredSizes, String targetSize) {
        if (targetSize == null) {
            throw new MatchException(NULL_TARGET_SIZE);
        }

        // 디버그 로그 추가
        System.out.println("선호 크기: " + preferredSizes);
        System.out.println("대상 크기: " + targetSize);

        List<String> normalizedPreferredSizes = preferredSizes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        String normalizedTargetSize = targetSize.toUpperCase();

        // 로그 추가
        System.out.println("정규화된 선호 크기: " + normalizedPreferredSizes);
        System.out.println("정규화된 대상 크기: " + normalizedTargetSize);

        if (normalizedPreferredSizes.contains(normalizedTargetSize)) {
            int totalPreferred = normalizedPreferredSizes.size();

            if (totalPreferred == 1) {
                if (normalizedPreferredSizes.contains("SMALL") || normalizedPreferredSizes.contains("LARGE")) {
                    return 20.0;
                }
                return 18.0;
            }
            if (totalPreferred == 2) {
                return 16.0;
            }
            if (totalPreferred == 3) {
                return 20.0 * 0.7;
            }
        }

        return 6.0; // 선호 크기에 포함되지 않을 경우 점수 하락
    }

}