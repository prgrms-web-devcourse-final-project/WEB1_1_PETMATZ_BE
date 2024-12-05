package com.petmatz.domain.petmission;

import com.petmatz.api.petmission.dto.PetMissionUpdateRequest;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.petmission.component.UserToChatRoomReader;
import com.petmatz.domain.petmission.component.UserToPetMissionInserter;
import com.petmatz.domain.petmission.component.UserToPetMissionReader;
import com.petmatz.domain.petmission.dto.PetMissionData;
import com.petmatz.domain.petmission.dto.PetMissionInfo;
import com.petmatz.domain.petmission.dto.UserToPetMissionListInfo;
import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
import com.petmatz.domain.petmission.entity.PetMissionEntity;
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

    public PetMissionData insertPetMission(PetMissionInfo petMissionInfo) {
        System.out.println("petMissionInfo.petMissionAskInfo :: " + petMissionInfo.petMissionAskInfo());
        Long careId = jwtExtractProvider.findIdFromJwt();

        List<User> users = makeUserEntityList(careId, petMissionInfo.receiverId());
        String chatRoomId = userToChatRoomReader.selectChatRoomId(users.get(0).getAccountId(), users.get(1).getAccountId());

        //TODO 예외처리 및 로직 다듬어야 함
        Optional<List<Pet>> petListByPetId = petRepository.findPetListByPetId(petMissionInfo.petId());
        List<Pet> pets = petListByPetId.get();

        List<PetMissionAskEntity> petMissionAskEntityList = petMissionInfo.petMissionAskInfo().stream().map(PetMissionAskEntity::of).toList();


        List<PetMissionEntity> petMissionEntities = pets.stream()
                .map(pet -> {
                    PetMissionEntity of = PetMissionEntity.of(petMissionInfo, pet);
                    of.addPetMissionAsk(petMissionAskEntityList);
                    return of;
                })
                .toList();


        List<UserToPetMissionEntity> userToPetMissionEntities = users.stream()
                .flatMap(user -> petMissionEntities.stream()
                        .map(petMissionEntity -> UserToPetMissionEntity.of(user, petMissionEntity)))
                .toList();
//
        userToPetMissionInserter.insertUserToPetMission(userToPetMissionEntities);

//        //TODO 반환 DTO 만들기
        return PetMissionData.of(chatRoomId,userToPetMissionEntities);

    }

    public List<UserToPetMissionEntity> selectPetMissionList() {
        Long userId = jwtExtractProvider.findIdFromJwt();
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

    public void selectPetMissionInfo(String petMissionId) {
        Long userId = jwtExtractProvider.findIdFromJwt();

        UserToPetMissionEntity userToPetMissionEntity = userToPetMissionReader.selectUserToPetMission(petMissionId, userId);

    }

    private List<User> makeUserEntityList(Long careId, Long receiverId) {
        ArrayList<User> userList = new ArrayList<>();
         userList.add(userRepository.findById(careId).get());
         userList.add(userRepository.findById(receiverId).get());
        return userList;
    }


}

