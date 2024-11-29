package com.petmatz.api.sosboard;

import com.petmatz.api.global.dto.Response;
import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.api.sosboard.dto.SosBoardCreateRequestDto;
import com.petmatz.api.sosboard.dto.SosBoardResponseDto;
import com.petmatz.domain.sosboard.SosBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import java.util.List;

@RestController
@RequestMapping("/api/sosboard")
@RequiredArgsConstructor
public class SosBoardController {

    private final SosBoardService sosBoardService;

    // 1. 돌봄 SOS 페이지 글 전체 조회 (지역별 필터링 + 페이지네이션)
    @GetMapping
    @Operation(summary = "SOS 게시글 전체 조회", description = "지역별 필터링 및 인덱스 기반 페이지네이션으로 SOS 게시글을 조회합니다.")
    @Parameters({
            @Parameter(name = "region", description = "필터링할 지역명", example = "Seoul"),
            @Parameter(name = "lastIndex", description = "마지막 조회 인덱스 (페이징 지원)", example = "100"),
            @Parameter(name = "size", description = "가져올 게시글 개수", example = "10")
    })
    public ResponseEntity<Response<List<SosBoardResponseDto>>> getAllSosBoards(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long lastIndex,
            @RequestParam(defaultValue = "10") int size) {

        List<SosBoardResponseDto> sosBoards = sosBoardService.getAllSosBoards(region, lastIndex, size);
        return ResponseEntity.ok(Response.success(sosBoards));
    }

    // 2. 돌봄 SOS 페이지 게시글 작성
    @PostMapping
    @Operation(summary = "SOS 게시글 작성", description = "SOS 게시판에 새로운 게시글을 작성합니다.")
    public SosBoardResponseDto createSosBoard(@RequestBody SosBoardCreateRequestDto requestDto) {
        return sosBoardService.createSosBoard(requestDto);
    }

    // 3. 해당 User의 펫 정보 불러오기
    @GetMapping("/pets/{userId}")
    @Operation(summary = "사용자 반려동물 조회", description = "사용자의 모든 반려동물 정보를 조회합니다.")
    @Parameter(name = "userId", description = "사용자 ID", example = "1")
    public List<PetResponse> getUserPets(@PathVariable Long userId) {
        return sosBoardService.getUserPets(userId);
    }

    // 자신이 작성한 게시글 조회 API
    @GetMapping("/user/{nickname}")
    @Operation(summary = "사용자가 작성한 SOS 게시글 조회", description = "특정 사용자가 작성한 SOS 게시글을 조회합니다.")
    @Parameters({
            @Parameter(name = "nickname", description = "사용자 닉네임", example = "test_nickname"),
            @Parameter(name = "page", description = "페이지 번호", example = "0"),
            @Parameter(name = "size", description = "가져올 게시글 개수", example = "10")
    })
    public ResponseEntity<Response<List<SosBoardResponseDto>>> getUserSosBoardsByNickname(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<SosBoardResponseDto> sosBoards = sosBoardService.getUserSosBoardsByNickname(nickname, page, size);
        return ResponseEntity.ok(Response.success(sosBoards));
    }
}

