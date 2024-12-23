package com.petmatz.api.pet;

import com.petmatz.api.global.dto.Response;

import com.petmatz.api.global.dto.S3ImgDataResponse;
import com.petmatz.api.pet.dto.PetApiRequest;
import com.petmatz.api.pet.dto.PetInfoDto;
import com.petmatz.api.pet.dto.PetRequest;
import com.petmatz.api.pet.dto.PetUpdateRequest;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.global.S3ImgDataInfo;
import com.petmatz.domain.pet.PetService;
import com.petmatz.domain.pet.vo.PetInf;
import com.petmatz.domain.pet.vo.PetUpdateInfo;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.net.MalformedURLException;


@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    //TODO 바꿔야 됨
//    private final UserService userService;

    private final UserRepository userRepository;
    private final JwtExtractProvider jwtExtractProvider;

    // 동물등록번호 조회
    @PostMapping("/fetch")
    @Operation(summary = "동물등록번호 조회", description = "외부 API를 통해 동물등록번호 정보를 조회합니다.")
    public Response<PetInfoDto> fetchPetInfo(@RequestBody PetApiRequest request) {
        PetInf serviceDto = PetApiRequest.toServiceDto(request);
        PetInf fetchedServiceDto = petService.fetchPetInfo(serviceDto.dogRegNo(), serviceDto.ownerNm());
        PetInfoDto responseDto = PetInfoDto.of(fetchedServiceDto);
        return Response.success(responseDto);
    }

    // 댕댕이 정보 등록
    @PostMapping("/register")
    @Operation(summary = "반려동물 등록", description = "사용자의 반려동물 정보를 등록합니다.")
    public Response<S3ImgDataResponse> registerPet(@RequestBody PetRequest request) throws MalformedURLException {
        User user = getAuthenticatedUser();
        S3ImgDataInfo petSaveInfo = petService.savePet(user, PetInf.of(request));
        return Response.success(S3ImgDataResponse.of(petSaveInfo));
    }

    // 댕댕이 정보 수정
    @PutMapping("/{id}")
    @Operation(summary = "반려동물 정보 수정", description = "기존 반려동물 정보를 수정합니다.")
    @Parameter(name = "id", description = "반려동물 ID", example = "1")
    public Response<S3ImgDataResponse> updatePet(@PathVariable Long id, @RequestBody PetUpdateRequest petUpdateRequest) throws MalformedURLException {
        User user = getAuthenticatedUser();
        S3ImgDataInfo petSaveInfo = petService.updatePet(id, user, PetUpdateInfo.of(petUpdateRequest));
        return Response.success(S3ImgDataResponse.of(petSaveInfo));
    }

    // 댕댕이 정보 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "반려동물 삭제", description = "등록된 반려동물을 삭제합니다.")
    @Parameter(name = "id", description = "반려동물 ID", example = "1")
    public Response<Void> deletePet(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        petService.deletePet(id, user);
        return Response.success("댕댕이 정보가 성공적으로 삭제되었습니다.");
    }

    private User getAuthenticatedUser() {
            Long userId = jwtExtractProvider.findIdFromJwt();
            return userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found :" + userId));
    }
}









