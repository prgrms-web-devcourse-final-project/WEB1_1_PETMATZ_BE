package com.petmatz.api.main_page;

import com.petmatz.api.global.dto.Response;
import com.petmatz.api.main_page.dto.MainPagePetMissionResponse;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.petmission.PetMissionService;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/main")
public class MainPageController {


    private final PetMissionService petMissionService;
    private final JwtExtractProvider jwtExtractProvider;

    @Operation(summary = "랜딩 페이지 사용자 미션 조회 [ 당일 ]", description = "랜딩 페이지 사용자 미션 조회 [ 당일 ] API")
    @GetMapping
    public Response<List<MainPagePetMissionResponse>> getUserSchedule() {
        Long userId = jwtExtractProvider.findIdFromJwt();
        List<UserToPetMissionEntity> userToPetMissionEntities = petMissionService.selectPetMissionList(userId, LocalDate.now());
        return Response.success(userToPetMissionEntities.stream().map(
                MainPagePetMissionResponse::of
        ).toList());
    }


}
