package com.petmatz.api.chatting;

import com.petmatz.api.chatting.dto.ChatRoomMetaDataInfoResponse;
import com.petmatz.api.chatting.dto.MatchRequest;
import com.petmatz.api.global.dto.Response;

import com.petmatz.domain.chatting.ChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    //TODO 채팅방 생성시 동작하도록  ( 구독 )
    //TODO 채팅방 고유 ID 가 NULL이면 없는 채팅방으로 간주하고 새로 생성한다.
    //TODO caregiverEmail -> JWT으로 바꾸기
    @PostMapping
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성하는 API API")
    @Parameters({
            @Parameter(name = "JWT [ caregiverInfo 추후 대체 ]", description = "토큰을 받아와 쓸 예정", example = "입력금지"),
            @Parameter(name = "caregiverEmail", description = "반려인 닉네임", example = "반려인이름"),
            @Parameter(name = "entrustedEmail", description = "돌봄이 닉네임", example = "돌봄이이름")
    })
    public Response<Long> matchUsers(@RequestBody MatchRequest matchRequest) {
        long chatRoomNumber = chatRoomService.createdChatRoom(matchRequest.of());
        return Response.success(chatRoomNumber);
    }

    // TODO user정보 포함시켜서 보내기
    @GetMapping
    @Operation(summary = "채팅방 조회", description = "해당 사용자가 보유하고 있는 채팅방 조회 API")
    @Parameters({
            @Parameter(name = "pageSize", description = "default : 5", example = "5"),
            @Parameter(name = "startPage", description = "default : 1", example = "1"),
    })
    public Response<List<ChatRoomMetaDataInfoResponse>> chatRoomsList(
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "1") int startPage
    ) {

        return Response.success(
                chatRoomService.selectChatRoomList(pageSize, startPage).stream()
                        .map(ChatRoomMetaDataInfoResponse::of)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping
    @Operation(summary = "채팅방 삭제", description = "해당 사용자가 지정한 채팅방을 삭제한다")
    @Parameters({
            @Parameter(name = "romId", description = "삭제하려는 Chat Room Id", example = "1"),
    })
    public Response<Void> deleteChatRoom(@RequestParam String roomId) {
        chatRoomService.deletRoom(roomId);
        return Response.success("성공적으로 삭제 되었습니다.");
    }

}
