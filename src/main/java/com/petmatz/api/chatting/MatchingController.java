package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.ChatRoomMetaDataInfoResponse;
import com.petmatz.api.chatting.dto.MatchRequest;
import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.chatting.ChatRoomService;
import com.petmatz.domain.chatting.dto.ChatRoomMetaDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/match")
public class MatchingController {

    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 매칭시 반려인과 돌봄이의 닉네임을 가져와야 한다.
     * @param matchRequest
     * @return
     */
    //TODO 반려인 닉네임 -> Token
    @PostMapping
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성하는 API API")
    @Parameters({
            @Parameter(name = "caregiverInfo", description = "반려인 닉네임", example = "반려인이름"),
            @Parameter(name = "entrustedName", description = "돌봄이 닉네임", example = "돌봄이이름")
    })
    public Response<Long> matchUsers(@RequestBody MatchRequest matchRequest) {
        long chatRoomNumber = chatRoomService.createdChatRoom(matchRequest.of());
        return Response.success(chatRoomNumber);
    }

    // TODO user정보 포함시켜서 보내기
    @GetMapping
    @Operation(summary = "채팅방 조회", description = "해당 사용자가 보유하고 있는 채팅방 조회 API")
    @Parameters({
            @Parameter(name = "JWT", description = "토큰을 받아와 쓸 예정", example = "입력금지"),
            @Parameter(name = "userEmail", description = "현재 자기 자신의 userEmail", example = "테스트"),
            @Parameter(name = "pageSize", description = "default : 5", example = "5"),
            @Parameter(name = "startPage", description = "default : 1", example = "1"),
    })
    public Response<Map<String,ChatRoomMetaDataInfoResponse>> chatRoomsList(
            @RequestParam String userEmail,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "1") int startPage
    ) {
        Map<String,ChatRoomMetaDataInfo> chatRoomList = chatRoomService.getChatRoomList(userEmail, pageSize, startPage);
        System.out.println(chatRoomList);
        return Response.success(chatRoomList.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // 람다로 키를 가져옴
                        entry -> ChatRoomMetaDataInfoResponse.of(entry.getValue()) // 값 변환
                )));
    }

    @DeleteMapping
    @Operation(summary = "채팅방 삭제", description = "해당 사용자가 지정한 채팅방을 삭제한다")
    public Response<?> deleteChatRoom(
            @RequestParam String userEmail,
            @RequestParam String roomId

    ) {
        chatRoomService.deletRoom(userEmail, roomId);
        return null;
    }

}
