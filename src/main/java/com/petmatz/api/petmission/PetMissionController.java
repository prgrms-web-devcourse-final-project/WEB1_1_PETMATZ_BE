package com.petmatz.api.petmission;

import com.petmatz.api.global.dto.Response;
import com.petmatz.api.petmission.dto.PetMissionRequest;
import com.petmatz.api.petmission.dto.PetMissionResponse;
import com.petmatz.api.petmission.dto.PetMissionUpdateRequest;
import com.petmatz.common.constants.PetMissionStatusZip;
import com.petmatz.domain.petmission.PetMissionService;
import com.petmatz.domain.petmission.dto.PetMissionData;
import com.petmatz.domain.petmission.dto.UserToPetMissionListInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pet/mission")
@RequiredArgsConstructor
public class PetMissionController {

    private final PetMissionService petMissionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Operation(summary = "멍멍이의 부탁 등록", description = "멍멍이의 부탁을 등록하는 API")
    @Parameters({
            @Parameter(name = "receiverId", description = "돌봄이 ID", example = "2"),
            @Parameter(name = "petId", description = "채팅방 번호", example = "10"),
            @Parameter(name = "missionStarted", description = "멍멍이의 부탁 시작일", example = "히힛"),
            @Parameter(name = "missionEnd", description = "멍멍이의 부탁 마감", example = "하핫"),
            @Parameter(name = "petMissionAsk", description = "멍멍이의 부탁 [ List<String> ]", example = "호수공원 산책하기")
    })
    @PostMapping
    public Response<PetMissionResponse> savePetMissionList(@RequestBody PetMissionRequest petMissionRequest) {
        PetMissionData petMissionData = petMissionService.insertPetMission(petMissionRequest.of());
        String destination = "/topic/chat/" + petMissionData.chatRoomId();
        PetMissionResponse petMissionResponse = PetMissionResponse.of(petMissionData);
        simpMessagingTemplate.convertAndSend(destination, petMissionResponse);
        return Response.success(petMissionResponse);
    }

    @Operation(summary = "멍멍이의 부탁 리스트 조회", description = "멍멍이 리스트 조회 API")
    @GetMapping
    public Response<List<UserToPetMissionListInfo>> selectPetMissionList() {
        List<UserToPetMissionListInfo> userToPetMissionListInfos = petMissionService.selectPetMissionList();
        return Response.success(userToPetMissionListInfos);
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

    @GetMapping("/Info")
    public void selectPetMissionInfo(@RequestParam("petMissionId") String petMissionId) {

        petMissionService.selectPetMissionInfo(petMissionId);

    }

/*
    @DeleteMapping
    public void deletePetMissionList() {

    }
*/

}
