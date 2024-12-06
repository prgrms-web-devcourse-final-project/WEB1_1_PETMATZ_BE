package com.petmatz.api.petmission;

import com.petmatz.api.global.dto.Response;
import com.petmatz.api.petmission.dto.PetMissionCommentRequest;
import com.petmatz.api.petmission.dto.PetMissionRequest;
import com.petmatz.api.petmission.dto.PetMissionResponse;
import com.petmatz.api.petmission.dto.PetMissionUpdateRequest;
import com.petmatz.common.constants.PetMissionStatusZip;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.chatting.ChatMessageService;
import com.petmatz.domain.chatting.dto.ChatMessageInfo;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetService;
import com.petmatz.domain.petmission.PetMissionService;
import com.petmatz.domain.petmission.dto.PetMissionData;
import com.petmatz.domain.petmission.dto.PetMissionDetails;
import com.petmatz.domain.petmission.dto.UserToPetMissionListInfo;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pet/mission")
@RequiredArgsConstructor
public class PetMissionController {

    private final PetMissionService petMissionService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatService;
    private final UserService userService;
    private final PetService petService;

    private final JwtExtractProvider jwtExtractProvider;

    @Operation(summary = "멍멍이의 부탁 등록", description = "멍멍이의 부탁을 등록하는 API")
    @Parameters({
            @Parameter(name = "receiverId", description = "돌봄이 ID", example = "2"), //-> 레포에서 조회 해서 가져와햐 함.
            @Parameter(name = "petId", description = "펫 ID [ List<String> ] ", example = "[1,2,3]"),
            @Parameter(name = "missionStarted", description = "멍멍이의 부탁 시작일", example = "히힛"),
            @Parameter(name = "missionEnd", description = "멍멍이의 부탁 마감", example = "하핫"),
            @Parameter(name = "petMissionAsk", description = "멍멍이의 부탁 [ List<String> ]", example = "[호수공원 산책하기, 놀아주기]")
    })
    @PostMapping
    public Response<PetMissionResponse> savePetMissionList(@RequestBody PetMissionRequest petMissionRequest) {
        Long careId = jwtExtractProvider.findIdFromJwt();

        String receiverEmail = userService.findByUserEmail(petMissionRequest.receiverId());
        PetMissionData petMissionData = petMissionService.insertPetMission(petMissionRequest.of(), careId);

        chatService.updateMessage(petMissionRequest.ofto(), petMissionData.chatRoomId(), receiverEmail);

        String destination = "/topic/chat/" + petMissionData.chatRoomId();
        //채팅 메세지에 UUID 담아서 보내기
        PetMissionResponse petMissionResponse = PetMissionResponse.of(petMissionData);
        simpMessagingTemplate.convertAndSend(destination, petMissionResponse);
        return Response.success(petMissionResponse);
    }

    @Operation(summary = "멍멍이의 부탁 리스트 조회", description = "멍멍이 리스트 조회 API")
    @GetMapping
    public Response<List<UserToPetMissionListInfo>> selectPetMissionList() {
        Long userId = jwtExtractProvider.findIdFromJwt();

        List<UserToPetMissionEntity> userToPetMissionEntities = petMissionService.selectPetMissionList(userId);
        List<UserToPetMissionListInfo> list = userToPetMissionEntities.stream().map(UserToPetMissionListInfo::of
        ).toList();
        return Response.success(list);
    }

    @Operation(summary = "멍멍이의 부탁 상태 업데이트", description = "멍멍이의 부탁 상태를 업데이트 하는 API")
    @Parameters({
            @Parameter(name = "petMissionId", description = "펫 미션 ID", example = "2"),
            @Parameter(name = "missionStatusZip", description = "미션 상태 [ PLG, END ]", example = "PLG"),
    })
    @PutMapping
    public Response<Void> updatePetMissionStatus(@RequestBody PetMissionUpdateRequest petMissionUpdateRequest) {
        petMissionService.updatePetMissionStatus(petMissionUpdateRequest);
        return Response.success("업데이트가 정상적으로 되었습니다.");
    }

    //------------------------아래는 잠시 보류 ---------------//

    @Operation(summary = "펫 미션 상세 조회", description = "펫 미션 상세 조회 API")
    @Parameters({
            @Parameter(name = "petMissionId", description = "펫 미션 ID", example = "2"),
    })
    @GetMapping("/Info")
    public Response<PetMissionDetails> selectPetMissionInfo(@RequestParam("petMissionId") String petMissionId) {
        PetMissionDetails petMissionDetails = petMissionService.selectPetMissionInfo(petMissionId);
        return Response.success(petMissionDetails);
    }

    //TODO comment에 ask달때 사진 s3로 전송해야 함.
    @PostMapping("/comment")
    public String saveComment(@RequestBody PetMissionCommentRequest petMissionCommentRequest) throws MalformedURLException {
        String userEmail = jwtExtractProvider.findAccountIdFromJwt();
        return petMissionService.updatePetMissionComment(petMissionCommentRequest.of(), userEmail);
    }

/*
    @DeleteMapping
    public void deletePetMissionList() {

    }
*/

}
