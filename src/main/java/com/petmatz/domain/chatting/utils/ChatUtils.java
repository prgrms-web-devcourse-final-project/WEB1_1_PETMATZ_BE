package com.petmatz.domain.chatting.utils;

public class ChatUtils {

    public static String addString(String chatRoomID, String userEmail) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chatRoomID);
        stringBuilder.append("_");
        stringBuilder.append(userEmail);
        return stringBuilder.toString();
    }

}
