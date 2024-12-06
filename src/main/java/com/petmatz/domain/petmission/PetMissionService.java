package com.petmatz.domain.petmission;

import com.petmatz.api.petmission.dto.PetMissionUpdateRequest;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.petmission.component.PetMissionReader;
import com.petmatz.domain.petmission.component.UserToChatRoomReader;
import com.petmatz.domain.petmission.component.UserToPetMissionInserter;
import com.petmatz.domain.petmission.component.UserToPetMissionReader;
import com.petmatz.domain.petmission.dto.*;
import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
import com.petmatz.domain.petmission.entity.PetToPetMissionEntity;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetMissionService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    private final UserToChatRoomReader userToChatRoomReader;
    private final JwtExtractProvider jwtExtractProvider;
    private final UserToPetMissionInserter userToPetMissionInserter;
    private final UserToPetMissionReader userToPetMissionReader;
    private final PetMissionReader petMissionReader;

    public PetMissionData insertPetMission(PetMissionInfo petMissionInfo, Long careId) {

        List<User> users = makeUserEntityList(careId, petMissionInfo.receiverId());

        String chatRoomId = userToChatRoomReader.selectChatRoomId(users.get(0).getAccountId(), users.get(1).getAccountId());

        List<Pet> pets = petRepository.findPetListByPetId(petMissionInfo.petId())
                .orElseThrow(() -> new IllegalArgumentException("No Pets Found"));

        List<PetMissionAskEntity> petMissionAskEntityList = petMissionInfo.petMissionAskInfo()
                .stream()
                .map(PetMissionAskEntity::of)
                .toList();

        PetMissionEntity petMissionEntity = PetMissionEntity.of(petMissionInfo);
        petMissionEntity.addPetMissionAsk(petMissionAskEntityList);

        List<PetToPetMissionEntity> petToPetMissionEntities = pets.stream()
                .map(pet -> PetToPetMissionEntity.of(pet, petMissionEntity))
                .toList();

        petToPetMissionEntities.forEach(petMissionEntity::addPetToPetMission);

        List<UserToPetMissionEntity> userToPetMissionEntities = users.stream()
                .map(user -> UserToPetMissionEntity.of(user, petMissionEntity, careId))
                .toList();

        userToPetMissionInserter.insertUserToPetMission(userToPetMissionEntities);

        return PetMissionData.of(chatRoomId, petMissionEntity);
    }

    public List<UserToPetMissionEntity> selectPetMissionList(Long userId) {
        return userToPetMissionReader.selectUserToPetMissionList(userId);
    }

    @Transactional
    public void updatePetMissionStatus(PetMissionUpdateRequest petMissionUpdateRequest) {
        jwtExtractProvider.findIdFromJwt();
        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionReader.selectUserToPetMissionList(petMissionUpdateRequest);
        for (UserToPetMissionEntity userToPetMissionEntity : userToPetMissionEntities) {
            PetMissionEntity petMission = userToPetMissionEntity.getPetMission();
            petMission.updatePetMissionStatusZip(petMissionUpdateRequest.missionStatusZip());
        }
    }

    public PetMissionDetails selectPetMissionInfo(String petMissionId) {
        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionReader.selectUserToPetMissionList(petMissionId);

        PetMissionEntity petMissionEntity = petMissionReader.selectUserToPetMission(petMissionId);
        System.out.println(petMissionEntity.toString());

        return PetMissionDetails.of(petMissionEntity, userToPetMissionEntities);
    }

    private List<User> makeUserEntityList(Long careId, Long receiverId) {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(userRepository.findById(careId).get());
        userList.add(userRepository.findById(receiverId).get());
        return userList;
    }


//    public PetMissionCommentInfo insertPetMissionComment() {
//
//    }
}

