package com.petmatz.api.main_page;

import com.petmatz.api.global.dto.Response;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.petmission.PetMissionService;
import com.petmatz.domain.petmission.dto.UserToPetMissionListInfo;
import com.petmatz.domain.petmission.entity.UserToPetMissionEntity;
import com.petmatz.domain.sosboard.SosBoard;
import com.petmatz.domain.sosboard.SosBoardServiceImpl;
import com.petmatz.domain.sosboard.dto.PageResponseDto;
import com.petmatz.domain.sosboard.dto.SosBoardServiceDto;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/main")
public class MainPageController {


    private final PetMissionService petMissionService;
    private final JwtExtractProvider jwtExtractProvider;

    @Operation(summary = "랜딩 페이지 사용자 미션 조회 [ 당일 ]", description = "랜딩 페이지 사용자 미션 조회 [ 당일 ] API")
    @GetMapping
    public Response<List<UserToPetMissionListInfo>> getUserSchedule() {
        Long userId = jwtExtractProvider.findIdFromJwt();
        List<UserToPetMissionEntity> userToPetMissionEntities = petMissionService.selectPetMissionList(userId, LocalDate.now());

        List<UserToPetMissionListInfo> list = userToPetMissionEntities.stream().map(
                userToPetMissionEntity ->UserToPetMissionListInfo.of(userToPetMissionEntity, petMissionService.selectUserToPetMissionList(String.valueOf(userToPetMissionEntity.getPetMission().getId())))
        ).toList();
        return Response.success(list);
    }


}
