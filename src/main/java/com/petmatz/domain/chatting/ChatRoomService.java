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
        if (chatRoomEntity.isPresent()) {
            return chatRoomEntity.get().getId();
        }

        long chatRoomId = chatRoomAppend.append(chatRoomInfo);

        chatDocsAppend.init(chatRoomInfo,chatRoomId);
        return chatRoomId;
    }


    //TODO other 추가
    public Map<String,ChatRoomMetaDataInfo> getChatRoomList(String userEmail, int pageSize, int startPage) {
        List<ChatRoomEntity> chatRoomList = chatRoomReader.findChatRoomNumber(userEmail);
        List<String> roomNumberList = chatRoomList.stream().map(
                chatRoomEntity -> String.valueOf(chatRoomEntity.getId())
        ).toList();

        Map<String, Integer> unreadCountList = updateMessageStatus(chatRoomList, userEmail, pageSize, startPage);
        System.out.println("unreadCountList :: " + unreadCountList);
        //User 추가해서 아래의 메서드에 넘기기
        Map<String, IChatUserInfo> userList = getUserList(chatRoomList, userEmail);

        return chatRoomMetaDataReader.findChatRoomMetaDataInfo(roomNumberList, unreadCountList, userList);
    }

    private Map<String, IChatUserInfo> getUserList(List<ChatRoomEntity> roomList, String userName) {
        Map<String, IChatUserInfo> userList = new HashMap<>();
        for (ChatRoomEntity chatRoomEntity : roomList) {
            String findUserName = chatRoomEntity.getUser1().equals(userName)
                    ? chatRoomEntity.getUser2()
                    : chatRoomEntity.getUser1();
            //userRepository에서 findUserName으로 정보 조회
            IChatUserInfo teeest = IChatUserInfo.of(findUserName,"TEEEST");
            userList.put(String.valueOf(chatRoomEntity.getId()), teeest);
        }
        return userList;
    }

    private Map<String, Integer> updateMessageStatus(List<ChatRoomEntity> chatRoomNumber,String userEmail, int pageSize, int startPage) {
        Map<String, Integer> unreadCountList = new HashMap<>();
        for (ChatRoomEntity chatRoomEntity : chatRoomNumber) {
            String chatRoomId = String.valueOf(chatRoomEntity.getId());
            ChatReadStatusDocs chatReadStatusDocs = chatReadStatusReader.selectChatMessageLastStatus(chatRoomId, userEmail);
            LocalDateTime lastReadTimestamp = chatReadStatusDocs != null ? chatReadStatusDocs.getLastReadTimestamp() : null;
            Integer unreadCount = chatMessageReader.countChatMessagesHistoryToLastDataAndUserName(chatRoomId,userEmail,lastReadTimestamp, startPage, pageSize);
            System.out.println("unreadCount :: " + unreadCount);

            unreadCountList.put(chatRoomId, unreadCount);
        }
        return unreadCountList;
    }

    public void deletRoom(String userName, String roomId) {
        chatRoomDeleter.deleteDocs();
        chatMessageDeleter.deleteDocs();
        chatRoomMetaDataDeleter.deleteDocs();
        chatReadStatusDeleter.deleteDocs();
    }
}
