package com.petmatz.api.sosboard;

import com.petmatz.api.global.dto.Response;
import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.api.sosboard.dto.SosBoardCreateRequestDto;
import com.petmatz.api.sosboard.dto.SosBoardResponseDto;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.sosboard.SosBoardService;
import com.petmatz.domain.sosboard.dto.PageResponseDto;
import com.petmatz.domain.sosboard.dto.SosBoardServiceDto;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sosboard")
@RequiredArgsConstructor
public class SosBoardController {

    private final SosBoardService sosBoardService;
    private final UserRepository userRepository;
    private final JwtExtractProvider jwtExtractProvider;

    // 돌봄 SOS 페이지 글 전체 조회 (지역별 필터링 + 페이지네이션)
    @GetMapping
    @Operation(summary = "SOS 게시글 전체 조회", description = "지역별 필터링 및 페이지 번호 기반 페이지네이션으로 SOS 게시글을 조회합니다.")
    @Parameters({
            @Parameter(name = "region", description = "필터링할 지역명", example = "Seoul"),
            @Parameter(name = "pageNum", description = "조회할 페이지 번호 (1부터 시작)", example = "1"),
            @Parameter(name = "size", description = "가져올 게시글 개수", example = "10")
    })
    public Response<PageResponseDto<SosBoardResponseDto>> getAllSosBoards(
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "1") int pageNum, // 페이지 번호 기본값 1
            @RequestParam(defaultValue = "10") int size) { // 페이지 크기 기본값 10

        int adjustedPage = pageNum - 1;

        // 서비스 호출
        PageResponseDto<SosBoardServiceDto> serviceDtoPageResponse = sosBoardService.getAllSosBoards(region, adjustedPage, size);

        // SosBoardServiceDto → SosBoardResponseDto 변환
        List<SosBoardResponseDto> responseDtos = serviceDtoPageResponse.content().stream()
                .map(SosBoardResponseDto::fromServiceDto) // 변환 메서드 활용
                .collect(Collectors.toList());

        // PageResponseDto 변환
        PageResponseDto<SosBoardResponseDto> responseDtoPageResponse = new PageResponseDto<>(
                responseDtos,
                serviceDtoPageResponse.totalCount(),
                serviceDtoPageResponse.totalPages(),
                serviceDtoPageResponse.currentPage()
        );

        return Response.success(responseDtoPageResponse);
    }


    // 2. 돌봄 SOS 페이지 게시글 작성
    @PostMapping
    @Operation(summary = "SOS 게시글 작성", description = "SOS 게시판에 새로운 게시글을 작성합니다.")
    public Response<SosBoardResponseDto> createSosBoard(@RequestBody SosBoardCreateRequestDto requestDto) {
        User user = getAuthenticatedUser(); // 인증된 사용자 가져오기
        SosBoardServiceDto serviceDto = requestDto.toServiceDto(user); // 프레젠테이션 DTO → 서비스 DTO 변환
        // 서비스 계층 호출
        SosBoardServiceDto createdBoard = sosBoardService.createSosBoard(serviceDto);
        SosBoardResponseDto responseDto = SosBoardResponseDto.fromServiceDto(createdBoard);
        return Response.success(responseDto);
    }

    //돌봄 SOS 페이지 특정 게시글 조회
    @GetMapping("/{id}")
    @Operation(summary = "특정 SOS 게시글 조회", description = "게시글 ID를 통해 특정 SOS 게시글을 조회합니다.")
    @Parameter(name = "id", description = "조회할 게시글의 ID", example = "1")
    public Response<SosBoardResponseDto> getSosBoardById(@PathVariable Long id) {
        SosBoardServiceDto serviceDto = sosBoardService.getSosBoardById(id);

        SosBoardResponseDto responseDto = SosBoardResponseDto.fromServiceDto(serviceDto);

        return Response.success(responseDto);
    }

    // 돌봄 SOS 페이지 글 수정
    @PutMapping("/{id}")
    @Operation(summary = "SOS 게시글 수정", description = "SOS 게시판의 게시글을 수정합니다.")
    public Response<SosBoardResponseDto> updateSosBoard(
            @PathVariable Long id,
            @RequestBody SosBoardCreateRequestDto requestDto) {

        User user = getAuthenticatedUser(); // 인증된 사용자 가져오기
        SosBoardServiceDto serviceDto = requestDto.toServiceDto(user);
        SosBoardServiceDto updatedBoard = sosBoardService.updateSosBoard(id, serviceDto, user);
        SosBoardResponseDto responseDto = SosBoardResponseDto.fromServiceDto(updatedBoard);

        return Response.success(responseDto);
    }

    // // 돌봄 SOS 페이지 글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "SOS 게시글 삭제", description = "SOS 게시판의 게시글을 삭제합니다.")
    public Response<Void> deleteSosBoard(@PathVariable Long id) {
        User user = getAuthenticatedUser(); // 인증된 사용자 가져오기
        sosBoardService.deleteSosBoard(id, user);
        return Response.success("게시글이 성공적으로 삭제되었습니다.");
    }


    // 해당 User의 펫 정보 불러오기
    @GetMapping("/pets/{userId}")
    @Operation(summary = "사용자 반려동물 조회", description = "사용자의 모든 반려동물 정보를 조회합니다.")
    @Parameter(name = "userId", description = "사용자 ID", example = "1")
    public Response<List<PetResponse>> getUserPets(@PathVariable Long userId) {
        List<PetResponse> userPets = sosBoardService.getUserPets(userId);
        return Response.success(userPets);
    }

    // 자신이 작성한 게시글 조회 API
    @GetMapping("/user/{nickname}")
    @Operation(summary = "사용자가 작성한 SOS 게시글 조회", description = "특정 사용자가 작성한 SOS 게시글을 조회합니다.")
    @Parameters({
            @Parameter(name = "nickname", description = "사용자 닉네임", example = "test_nickname"),
            @Parameter(name = "pageNum", description = "페이지 번호", example = "1"),
            @Parameter(name = "size", description = "가져올 게시글 개수", example = "10")
    })
    public Response<PageResponseDto<SosBoardResponseDto>> getUserSosBoardsByNickname(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int size) {

        int adjustedPage = pageNum - 1;

        // 서비스 계층 호출
        PageResponseDto<SosBoardServiceDto> serviceDtoPageResponse = sosBoardService.getUserSosBoardsByNickname(nickname, adjustedPage, size);

        // SosBoardServiceDto → SosBoardResponseDto 변환
        List<SosBoardResponseDto> responseDtos = serviceDtoPageResponse.content().stream()
                .map(SosBoardResponseDto::fromServiceDto)
                .collect(Collectors.toList());

        // PageResponseDto 변환
        PageResponseDto<SosBoardResponseDto> responseDtoPageResponse = new PageResponseDto<>(
                responseDtos,
                serviceDtoPageResponse.totalCount(),
                serviceDtoPageResponse.totalPages(),
                serviceDtoPageResponse.currentPage()
        );

        return Response.success(responseDtoPageResponse);
    }


    private User getAuthenticatedUser() {
        Long userId = jwtExtractProvider.findIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found :" + userId));

        return user;
    }
}

