package com.petmatz.domain.petmission.component;

import com.petmatz.domain.chatting.repository.UserToChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserToChatRoomReader {

    private final UserToChatRoomRepository userToChatRoomRepository;

    public String selectChatRoomId(String careEmail, String receiverEmail) {
        Optional<String> s = userToChatRoomRepository.selectChatRoomIdForUser1ToUser2(careEmail, receiverEmail);
        return s.get();
    }

}
