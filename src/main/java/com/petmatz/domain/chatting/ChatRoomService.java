package com.petmatz.domain.chatting;

import com.petmatz.domain.chatting.component.*;
import com.petmatz.domain.chatting.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomAppend chatRoomAppend;
    private final ChatDocsAppend chatDocsAppend;

    private final ChatRoomReader chatRoomReader;
    private final ChatMessageReader chatMessageReader;
    private final ChatRoomMetaDataReader chatRoomMetaDataReader;
    private final ChatReadStatusReader chatReadStatusReader;

    private final ChatRoomDeleter chatRoomDeleter;
    private final ChatMessageDeleter chatMessageDeleter;
    private final ChatRoomMetaDataDeleter chatRoomMetaDataDeleter;
    private final ChatReadStatusDeleter chatReadStatusDeleter;




    
    //채팅방 신규 생성, 존재시 해당 ChatRoomID 반환
    public long createdChatRoom(ChatRoomInfo chatRoomInfo) {
        Optional<ChatRoomEntity> chatRoomEntity = chatRoomReader.selectChatRoom(chatRoomInfo);
        System.out.println(chatRoomEntity.toString());

        if (chatRoomEntity.isPresent()) {
            return chatRoomEntity.get().getId();
        }

        long chatRoomId = chatRoomAppend.append(chatRoomInfo);

        chatDocsAppend.init(chatRoomInfo,chatRoomId);
        return chatRoomId;
    }


    //TODO other 추가
    public Map<String,ChatRoomMetaDataInfo> getChatRoomList(String userEmail, int pageSize, int startPage) {
        List<UserToChatRoomEntity> chatRoomNumber = chatRoomReader.findChatRoomNumber(userEmail);
        List<String> roomNumberList = chatRoomNumber.stream()
                .map(chatRoomEntity -> String.valueOf(chatRoomEntity.getChatRoom().getId()))
                .distinct()
                .toList();

        Map<String, Integer> unreadCountList = updateMessageStatus(roomNumberList, userEmail, pageSize, startPage);
        //User 추가해서 아래의 메서드에 넘기기
        Map<String, IChatUserInfo> userList = getUserList(chatRoomNumber, userEmail);

        return chatRoomMetaDataReader.findChatRoomMetaDataInfo(roomNumberList, unreadCountList, userList);
    }

    private Map<String, IChatUserInfo> getUserList(List<UserToChatRoomEntity> chatRoomNumber, String userEmail) {
        Map<String, IChatUserInfo> userList = new HashMap<>();
        for (UserToChatRoomEntity userToChatRoomEntity : chatRoomNumber) {
            List<UserToChatRoomEntity> participants = userToChatRoomEntity.getChatRoom().getParticipants();
            // 현재 사용자가 아닌 다른 사용자를 찾음
            for (UserToChatRoomEntity participant : participants) {
                if (!participant.getUser().getAccountId().equals(userEmail)) {
                    IChatUserInfo userInfo = IChatUserInfo.of(
                            participant.getUser().getAccountId(),
                            participant.getUser().getProfileImg()
                    );
                    userList.put(userToChatRoomEntity.getChatRoom().getId().toString(), userInfo);
                    break; // 다른 참여자를 하나만 찾으면 됨
                }
            }
        }
        return userList;
    }

    private Map<String, Integer> updateMessageStatus(List<String> chatRoomNumber,String userEmail, int pageSize, int startPage) {
        Map<String, Integer> unreadCountList = new HashMap<>();
        for (String roomId : chatRoomNumber) {
            String chatRoomId = String.valueOf(roomId);
            ChatReadStatusDocs chatReadStatusDocs = chatReadStatusReader.selectChatMessageLastStatus(chatRoomId, userEmail);
            LocalDateTime lastReadTimestamp = chatReadStatusDocs != null ? chatReadStatusDocs.getLastReadTimestamp() : null;
            Integer unreadCount = chatMessageReader.countChatMessagesHistoryToLastDataAndUserName(chatRoomId,userEmail,lastReadTimestamp, startPage, pageSize);
            unreadCountList.put(chatRoomId, unreadCount);
        }
        return unreadCountList;
    }

    public void deletRoom(String userName, String roomId) {
        List<String> strings = chatRoomReader.selectChatRoomUserList(roomId).get();
        chatRoomDeleter.deleteDocs(userName, roomId);
        chatMessageDeleter.deleteChatMessageDocs(userName, roomId);
        chatRoomMetaDataDeleter.deleteChatRoomMetaDataDocs(userName, roomId);
        chatReadStatusDeleter.deleteChatReadStatusDocs(strings, roomId);
    }
}
