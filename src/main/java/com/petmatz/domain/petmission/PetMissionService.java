//package com.petmatz.domain.petmission;
//
//import com.petmatz.common.security.utils.JwtExtractProvider;
//import com.petmatz.domain.pet.Pet;
//import com.petmatz.domain.pet.PetRepository;
//import com.petmatz.domain.petmission.component.PetMissionInseart;
//import com.petmatz.domain.petmission.component.PetMissionReader;
//import com.petmatz.domain.petmission.component.UserToChatRoomReader;
//import com.petmatz.domain.petmission.component.UserToPetMissionInserter;
//import com.petmatz.domain.petmission.dto.PetMissionData;
//import com.petmatz.domain.petmission.dto.PetMissionInfo;
//import com.petmatz.domain.petmission.dto.PetMissionListInfo;
//import com.petmatz.domain.petmission.entity.PetMissionAskEntity;
//import com.petmatz.domain.petmission.entity.PetMissionEntity;
//import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
//import com.petmatz.domain.petmission.repository.UserToPetMissionRepository;
//import com.petmatz.domain.user.entity.User;
//import com.petmatz.domain.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PetMissionService {
//
//    private final UserRepository userRepository;
//    private final PetRepository petRepository;
//
//    private final UserToChatRoomReader userToChatRoomReader;
//    private final PetMissionInseart petMissionInseart;
//    private final PetMissionReader petMissionReader;
//    private final JwtExtractProvider jwtExtractProvider;
//    private final UserToPetMissionInserter userToPetMissionInserter;
//
//    public PetMissionData insertPetMission(PetMissionInfo petMissionInfo) {
//        //ID 탐색
//        Long careId = jwtExtractProvider.findIdFromJwt();
//
//        List<User> users = makeUserEntityList(careId, petMissionInfo.receiverId());
//
//        //TODO 예외처리 해야 함
//        Optional<Pet> byId = petRepository.findById(Long.valueOf(petMissionInfo.petId()));
//        Pet pet = byId.get();
//
//        PetMissionEntity petMissionEntity = petMissionInseart.insertPetMission(PetMissionEntity.of(petMissionInfo, pet));
//
//        List<UserToPetMissionEntity> list = users.stream().map(
//                user -> UserToPetMissionEntity.of(user, petMissionEntity)
//        ).toList();
//
//        List<UserToPetMissionEntity> userToPetMissionEntities = userToPetMissionInserter.insertUserToPetMission(list);
//        String chatRoomId = userToChatRoomReader.selectChatRoomId(users.get(0).getEmail(), users.get(1).getEmail());
//
//
//        //TODO 반환 DTO 만들기
//
//        return PetMissionData.of(chatRoomId,userToPetMissionEntities);
//
//    }
//
//    public List<UserToPetMissionEntity> selectPetMissionList() {
//        Long userId = jwtExtractProvider.findIdFromJwt();
////        return petMissionReader.selectPetMissionId(userId).stream()
////                .map(UserToPetMissionEntity::of).toList();
//        return null;
//
//    }
//
//    private List<User> makeUserEntityList(Long careId, Long receiverId) {
//        ArrayList<User> userList = new ArrayList<>();
//         userList.add(userRepository.findById(careId).get());
//         userList.add(userRepository.findById(receiverId).get());
//        return userList;
//    }
//}
