package com.petmatz.api.petmission;

import com.petmatz.api.petmission.dto.PetMissionRequest;
import com.petmatz.domain.petmission.PetMissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


//TODD 돌봄 시작을 누르면 상태가 바뀌게 됨.코멘트필요
//넘어가는 정보
//        맡김이 id,
//        돌봄이 id,
//        대상 멍멍이 ( 여러마리 일 수 있음 ),
//        시작일시,
//        종료일시,
//        부탁
//        돌봄시작 상태값 { 시작 = PLG, 종료 = END }
//

// 미션 등록되면 -> 채팅방에 뿌려줌 ( 돌봄이/맡김이 UUID )
// 돌봄이/맡김이 UUID로 채팅방 조회 후

// 답변은 무조껀 하나, [ 수정만 가능하게 ]
@RestController
@RequestMapping("/api/v1/pet/mission")
@RequiredArgsConstructor
public class PetMissionController {

    private final PetMissionService petMissionService;

    @PostMapping
    public void savePetMissionList(@RequestBody PetMissionRequest petMissionRequest) {
        petMissionService.insertPetMission(petMissionRequest.of());
    }

    @GetMapping
    public void selectPetMissionList() {

    }

    @PutMapping
    public void updatePetMissionList() {

    }
    @DeleteMapping
    public void deletePetMissionList() {

    }

}