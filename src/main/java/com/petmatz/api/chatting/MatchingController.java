package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.ChatRoomListResponse;
import com.petmatz.api.chatting.dto.MatchRequest;
import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.chatting.ChatRoomService;
import com.petmatz.domain.chatting.dto.ChatRoomListInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성하는 API API")
    @Parameters({
            @Parameter(name = "caregiverInfo", description = "반려인 닉네임", example = "반려인이름"),
            @Parameter(name = "entrustedInfo", description = "돌봄이 닉네임", example = "돌봄이이름")
    })
    public Response<Long> matchUsers(@RequestBody MatchRequest matchRequest) {
        long chatRoomNumber = chatRoomService.createdChatRoom(matchRequest.of());
        return Response.success(chatRoomNumber);
    }

    // TODO userName은 추후 JWT으로 대체 될 예정
    @GetMapping
    @Operation(summary = "채팅방 조회", description = "해당 사용자가 보유하고 있는 채팅방 조회 API")
    @Parameters({
            @Parameter(name = "JWT", description = "토큰을 받아와 쓸 예정", example = "XXXXX"),
    })
    public Response<List<ChatRoomListInfo>> chatRoomsList() {
        String userName = "테스트";
        List<ChatRoomListInfo> chatRoomList = chatRoomService.getChatRoomList(userName);
        return Response.success(chatRoomList);
    }

}
