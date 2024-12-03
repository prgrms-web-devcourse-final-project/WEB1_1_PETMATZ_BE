package com.petmatz.domain.sosboard;

import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.domain.sosboard.dto.PageResponseDto;
import com.petmatz.domain.sosboard.dto.SosBoardServiceDto;
import com.petmatz.domain.user.entity.User;

import java.util.List;

public interface SosBoardService {

    // 전체 조회 (지역 필터링 + 인덱스 기반 페이지네이션)
    PageResponseDto<SosBoardServiceDto> getAllSosBoards(String region, int pageNum, int size);

    // 게시글 작성
    SosBoardServiceDto createSosBoard(SosBoardServiceDto serviceDto);

    // 특정 게시물 조회
    SosBoardServiceDto getSosBoardById(Long id);

    // 게시글 업데이트
    SosBoardServiceDto updateSosBoard(Long id, SosBoardServiceDto serviceDto, User user);

    // 게시글 삭제
    void deleteSosBoard(Long id, User user);

    // User의 Pet 정보 불러오기
    List<PetResponse> getUserPets(Long userId);

    // User의 게시글 닉네임 기반 조회 (페이지네이션)
    PageResponseDto<SosBoardServiceDto> getUserSosBoardsByNickname(String nickname, int page, int size);
}
