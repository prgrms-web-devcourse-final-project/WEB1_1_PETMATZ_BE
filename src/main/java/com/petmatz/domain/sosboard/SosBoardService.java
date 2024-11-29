package com.petmatz.domain.sosboard;

import com.petmatz.api.pet.dto.PetResponse;
import com.petmatz.api.sosboard.dto.SosBoardCreateRequestDto;
import com.petmatz.api.sosboard.dto.SosBoardResponseDto;
import com.petmatz.domain.pet.Pet;
import com.petmatz.domain.pet.PetRepository;
import com.petmatz.domain.sosboard.dto.SosBoardPetDto;
import com.petmatz.domain.sosboard.exception.SosBoardErrorCode;
import com.petmatz.domain.sosboard.exception.SosBoardServiceException;

import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SosBoardService {

    private final SosBoardRepository sosBoardRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    // 1. 전체 조회 (지역 필터링 + 인덱스 기반 페이지네이션)
    public List<SosBoardResponseDto> getAllSosBoards(String region, Long lastIndex, int size) {
        if (lastIndex == null) {
            lastIndex = Long.MAX_VALUE; // 첫 페이지 조회 시 최대값으로 시작
        }

        List<SosBoard> sosBoards;
        if (region == null || region.isEmpty()) {
            // 전체 조회
            sosBoards = sosBoardRepository.findAllByIdLessThanOrderByIdDesc(lastIndex, PageRequest.of(0, size));
        } else {
            // 지역 필터링
            sosBoards = sosBoardRepository.findByRegionAndIdLessThanOrderByIdDesc(region, lastIndex, PageRequest.of(0, size));
        }

        return sosBoards.stream()
                .map(SosBoardResponseDto::of) // SosBoard → SosBoardResponseDto 변환
                .collect(Collectors.toList());
    }

    // 2. 게시글 작성
    public SosBoardResponseDto createSosBoard(User user, SosBoardCreateRequestDto requestDto) {

        try {
            // SosBoard 엔티티 생성
            SosBoard sosBoard = SosBoard.builder()
                    .user(user)
                    .title(requestDto.title())
                    .region(requestDto.region())
                    .paymentType(SosBoard.PaymentType.fromString(requestDto.paymentType()))
                    .price(requestDto.price())
                    .comment(requestDto.comment())
                    .startDate(requestDto.startDate())
                    .endDate(requestDto.endDate())
                    .build();

            // 펫 확인 및 중간 테이블에 저장
            List<PetSosBoard> petSosBoards = requestDto.petIds().stream()
                    .map(petId -> {
                        Pet pet = petRepository.findById(petId)
                                .orElseThrow(() -> new SosBoardServiceException(SosBoardErrorCode.BOARD_NOT_FOUND));
                        return PetSosBoard.builder()
                                .sosBoard(sosBoard)
                                .pet(pet)
                                .build();
                    })
                    .collect(Collectors.toList());

            sosBoard.setPetSosBoards(petSosBoards); // SosBoard에 중간 테이블 설정

            // 저장 및 응답 반환
            return SosBoardResponseDto.of(sosBoardRepository.save(sosBoard));
        } catch (IllegalArgumentException e) {
            throw new SosBoardServiceException(SosBoardErrorCode.INVALID_INPUT, e.getMessage());
        } catch (Exception e) {
            throw new SosBoardServiceException(SosBoardErrorCode.DATABASE_ERROR, "SERVICE");
        }
    }

    /*@Override
    public void updateSosBoard(Long id, User user, SosBoardCreateRequestDto updatedRequest) {
        // ID로 게시글 조회
        SosBoard sosBoard = sosBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 사용자 권한 검증 (작성자만 수정 가능)
        if (!sosBoard.getUser().equals(user)) {
            throw new IllegalStateException("해당 게시글을 수정할 권한이 없습니다.");
        }

        // 게시글 내용 업데이트
        sosBoard.updateContent(updatedRequest.getTitle(), updatedRequest.getContent(), updatedRequest.getRegion());

        // 변경사항 저장
        sosBoardRepository.save(sosBoard);
    }*/

    // 3. User의 Pet 정보 불러오기
    public List<PetResponse> getUserPets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SosBoardServiceException(SosBoardErrorCode.USER_NOT_FOUND));

        // Pet 엔티티를 DTO로 변환
        return petRepository.findByUserId(user.getId()).stream()
                .map(SosBoardPetDto::of) // Pet → SosBoardPetDto 변환
                .map(PetResponse::of)    // SosBoardPetDto → PetResponse 변환
                .collect(Collectors.toList());
    }

    public List<SosBoardResponseDto> getUserSosBoardsByNickname(String nickname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<SosBoard> sosBoards = sosBoardRepository.findByUserNickname(nickname, pageable);

        return sosBoards.stream()
                .map(SosBoardResponseDto::of)
                .collect(Collectors.toList());
    }
}



