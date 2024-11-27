package com.petmatz.domain.petmission;

import com.petmatz.domain.chatting.repository.UserToChatRoomRepository;
import com.petmatz.domain.petmission.dto.PetMissionInfo;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetMissionService {

    private final UserRepository userRepository;
    private final UserToChatRoomRepository userToChatRoomRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void insertPetMission(PetMissionInfo petMissionInfo) {
        User careUserEntity = userRepository.findById(petMissionInfo.careId()).get();
        User receiverEntity = userRepository.findById(petMissionInfo.receiverId()).get();
        String careEmail = careUserEntity.getAccountId();
        String receiverEmail = receiverEntity.getAccountId();

        Optional<String> s = userToChatRoomRepository.selectChatRoomIdForUser1ToUser2(careEmail, receiverEmail);
        if (s.isEmpty()) {
            System.out.println("비었음");
        } else {
            System.out.println(s.get());
        }

    }
}
