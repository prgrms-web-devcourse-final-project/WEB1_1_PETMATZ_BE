package com.petmatz.api.pet;

import com.petmatz.api.global.dto.Response;

import com.petmatz.api.pet.dto.PetApiRequest;
import com.petmatz.api.pet.dto.PetInfoDto;
import com.petmatz.api.pet.dto.PetRequest;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.pet.PetServiceImpl;
import com.petmatz.domain.pet.dto.PetServiceDto;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetServiceImpl petServiceImpl;
    private final UserRepository userRepository;
    private final JwtExtractProvider jwtExtractProvider;

    // 동물등록번호 조회
    @PostMapping("/fetch")
    @Operation(summary = "동물등록번호 조회", description = "외부 API를 통해 동물등록번호 정보를 조회합니다.")
    public ResponseEntity<Response<PetInfoDto>> fetchPetInfo(@RequestBody PetApiRequest request) {
        PetServiceDto serviceDto = PetApiRequest.toServiceDto(request);
        PetServiceDto fetchedServiceDto = petServiceImpl.fetchPetInfo(serviceDto.dogRegNo(), serviceDto.ownerNm());
        PetInfoDto responseDto = PetInfoDto.of(fetchedServiceDto);
        return ResponseEntity.ok(Response.success(responseDto));
    }

    // 댕댕이 정보 등록
    @PostMapping("/register")
    @Operation(summary = "반려동물 등록", description = "사용자의 반려동물 정보를 등록합니다.")
    public ResponseEntity<Response<Void>> registerPet(@RequestBody PetRequest request) {
        User user = getAuthenticatedUser();
        PetServiceDto serviceDto = PetRequest.toServiceDto(request);
        petServiceImpl.savePet(user, serviceDto);
        return ResponseEntity.ok(Response.success("댕댕이가 성공적으로 등록되었습니다."));
    }

    // 댕댕이 정보 수정
    @PutMapping("/{id}")
    @Operation(summary = "반려동물 정보 수정", description = "기존 반려동물 정보를 수정합니다.")
    @Parameter(name = "id", description = "반려동물 ID", example = "1")
    public ResponseEntity<Response<Void>> updatePet(@PathVariable Long id, @RequestBody PetRequest updatedRequest) {
        User user = getAuthenticatedUser();
        PetServiceDto updatedServiceDto = PetServiceDto.of(updatedRequest);
        petServiceImpl.updatePet(id, user, updatedServiceDto);
        return ResponseEntity.ok(Response.success("댕댕이 정보가 업데이트 완료되었습니다."));
    }

    // 댕댕이 정보 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "반려동물 삭제", description = "등록된 반려동물을 삭제합니다.")
    @Parameter(name = "id", description = "반려동물 ID", example = "1")
    public ResponseEntity<Response<Void>> deletePet(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        petServiceImpl.deletePet(id, user);
        return ResponseEntity.ok(Response.success("댕댕이 정보가 성공적으로 삭제되었습니다."));
    }

    private User getAuthenticatedUser() {
        Long userId = jwtExtractProvider.findIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found :" + userId));

        return user;
    }
}









