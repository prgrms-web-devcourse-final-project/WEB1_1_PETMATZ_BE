package com.petmatz.api.pet;

import com.petmatz.api.global.dto.Response;

import com.petmatz.domain.pet.PetService;
import com.petmatz.domain.pet.PetServiceDto;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    // 동물등록번호 조회
    @PostMapping("/fetch")
    public ResponseEntity<Response<PetInfoDto>> fetchPetInfo(@RequestBody PetApiRequest request) {
        PetServiceDto serviceDto = PetApiRequest.toServiceDto(request);
        PetServiceDto fetchedServiceDto = petService.fetchPetInfo(serviceDto);
        PetInfoDto responseDto = PetInfoDto.of(fetchedServiceDto);
        return ResponseEntity.ok(Response.success(responseDto));
    }

    // 댕댕이 정보 등록
    @PostMapping("/register")
    public ResponseEntity<Response<Void>> registerPet(@RequestBody PetRequest request) {
        User user = getMockUser();
        PetServiceDto serviceDto = PetRequest.toServiceDto(request);
        petService.savePet(user, serviceDto);
        return ResponseEntity.ok(Response.success("댕댕이가 성공적으로 등록되었습니다."));
    }

    // 댕댕이 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Response<Void>> updatePet(@PathVariable Long id, @RequestBody PetRequest updatedRequest) {
        User user = getMockUser();
        PetServiceDto updatedServiceDto = PetServiceDto.of(updatedRequest);
        petService.updatePet(id, user, updatedServiceDto);
        return ResponseEntity.ok(Response.success("댕댕이 정보가 업데이트 완료되었습니다."));
    }

    //댕댕이 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deletePet(@PathVariable Long id) {
        User user = getMockUser();
        petService.deletePet(id, user);
        return ResponseEntity.ok(Response.success("댕댕이 정보가 성공적으로 삭제되었습니다."));
    }

    // 테스트 위한 MockUser (user 단 세팅 후 수정 예정)
    private User getMockUser() {
        return User.builder()
                .id(1L)
                .accountId("test_account")
                .nickname("test_nickname")
                .build();
    }
}








