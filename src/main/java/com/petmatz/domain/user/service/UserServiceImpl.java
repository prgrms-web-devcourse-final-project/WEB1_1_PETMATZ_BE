package com.petmatz.domain.user.service;

import com.petmatz.api.user.request.*;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.common.security.utils.JwtProvider;
import com.petmatz.domain.user.entity.Certification;
import com.petmatz.domain.user.entity.Heart;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.entity.UserFactory;
import com.petmatz.domain.user.info.*;
import com.petmatz.domain.user.provider.CertificationNumberProvider;
import com.petmatz.domain.user.provider.RePasswordProvider;
import com.petmatz.domain.user.repository.CertificationRepository;
import com.petmatz.domain.user.repository.HeartRepository;
import com.petmatz.domain.user.repository.UserRepository;
import com.petmatz.domain.user.response.*;
import com.petmatz.user.common.LogInResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final HeartRepository heartRepository;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;
    private final RePasswordEmailProvider rePasswordEmailProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtExtractProvider jwtExtractProvider;

    private final GeocodingService geocodingService;


    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String accountId = dto.getAccountId();

            User user = userRepository.findByAccountId(accountId);
            if (user != null && user.getIsDeleted() == true) {
                return EmailCertificationResponseDto.alreadyDeletedUser();
            }
            //이메일 전송과 동시에 아이디 중복검사
            boolean isExistId = userRepository.existsByAccountId(accountId);
            if (isExistId) return EmailCertificationResponseDto.duplicateId();

            // 인증 번호 생성 및 이메일 전송
            String certificationNumber = CertificationNumberProvider.generateNumber();
            boolean isSendSuccess = emailProvider.sendVerificationEmail(accountId, certificationNumber);
            if (!isSendSuccess) return EmailCertificationResponseDto.mailSendFail();

            // 인증 엔티티 저장
            Certification certification = Certification.builder().accountId(accountId).certificationNumber(certificationNumber).build();
            certificationRepository.save(certification);

        } catch (Exception e) {
            log.info("이메일 인증 실패: {}", e);
            return EmailCertificationResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationInfo info) {
        try {
            String accountId = info.getAccountId();
            String certificationNumber = info.getCertificationNumber();

            // 인증 엔티티 조회
            Certification Certification = certificationRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);
            if (Certification == null) return CheckCertificationResponseDto.certificationFail();

            // 이메일 및 인증 번호가 일치하는지 확인
            boolean isMatch = Certification.getAccountId().equals(accountId) && Certification.getCertificationNumber().equals(certificationNumber);
            if (!isMatch) return CheckCertificationResponseDto.certificationFail();

        } catch (Exception e) {
            log.info("인증 번호 확인 실패: {}", e);
            return LogInResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();  // 인증 성공 응답
    }

    @Override
    @Transactional
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpInfo info) {
        try {
            String accountId = info.getAccountId();
            String certificationNumber = info.getCertificationNumber();

            // 인증 번호 확인
            Certification certification = certificationRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);
            if (certification == null || !certification.getCertificationNumber().equals(certificationNumber)) {
                return SignUpResponseDto.certificationFail();
            }

            // 비밀번호 암호화 후 저장
            String password = info.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            info.setPassword(encodedPassword);

            // 외부 호출 분리
            String region = geocodingService.getRegionFromCoordinates(info.getLatitude(), info.getLongitude());

            User user = UserFactory.createNewUser(info, encodedPassword, region);
            userRepository.save(user);

            // 인증 엔티티 삭제
            certificationRepository.deleteAllByAccountId(accountId);

            return SignUpResponseDto.success();
        } catch (RuntimeException e) {
            log.error("회원 가입 실패: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("회원 가입 중 처리되지 않은 예외 발생: {}", e);
            throw new RuntimeException("회원 가입 중 처리되지 않은 예외", e);
        }
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInInfo info, HttpServletResponse response) {
        try {
            String accountId = info.getAccountId();
            User user = userRepository.findByAccountId(accountId);
            if (user != null && user.getIsDeleted() == true) {
                return EmailCertificationResponseDto.alreadyDeletedUser();
            }
            // 사용자 존재 여부 확인
            if (user == null) {
                log.info("사용자 조회 실패: {}", accountId);
                return SignInResponseDto.signInFail();
            }

            // 비밀번호 확인
            String password = info.getPassword();
            String encodedPassword = user.getPassword();
            if (!passwordEncoder.matches(password, encodedPassword)) {
                log.info("비밀번호 불일치: {}", accountId);
                return SignInResponseDto.signInFail();
            }

            // JWT 생성 (userId를 subject로, accountId를 클레임으로 설정)
            String token = jwtProvider.create(user.getId(), user.getAccountId());
            log.info("JWT 생성 완료: {}", token);


            ResponseCookie responseCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)           // XSS 방지
                    .secure(true)             // HTTPS만 허용
                    .path("/")                // 모든 경로에서 접근 가능
                    .sameSite("None")         // SameSite=None 설정
                    .maxAge((3600))
                    .build();
            response.addHeader("Set-Cookie", responseCookie.toString());
            // 로그인 성공 응답 반환
            return SignInResponseDto.success(user); // User 객체 전달
        } catch (Exception e) {
            log.error("로그인 처리 중 예외 발생", e);
            return SignInResponseDto.signInFail();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto) {
        try {
            Long userId = jwtExtractProvider.findIdFromJwt();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            // 비밀번호 일치 확인
            String password = dto.getPassword();
            String encodedPassword = user.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return DeleteIdResponseDto.idNotMatching();  // 비밀번호 불일치

            certificationRepository.deleteById(userId);

            user.setAccountId(user.getAccountId()); // 이메일 변경
            user.setPassword("deleted-password"); // 비밀번호 변경
            user.setNickname("Deleted User"); // 닉네임 변경
            user.setProfileImg(null); // 프로필 이미지 제거
            user.setIntroduction(null); // 소개 제거
            user.setPreferredSizes(null); // 선호 크기 초기화
            user.setIsCareAvailable(false); // 돌봄 가능 여부 초기화
            user.setRecommendationCount(0);
            user.setLatitude(null); // 위도 초기화
            user.setLongitude(null); // 경도 초기화
            user.setRegion(null); // 지역 초기화
            user.setIsDeleted(true); // 삭제 상태 플래그 설정

        } catch (Exception e) {
            log.info("회원 삭제 실패: {}", e);
            return DeleteIdResponseDto.databaseError();  // 데이터베이스 오류 처리
        }
        return DeleteIdResponseDto.success();  // 삭제 성공 응답
    }


    //    -------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseEntity<? super GetMyProfileResponseDto> getMypage() {
        try {
            String userId = jwtExtractProvider.findAccountIdFromJwt();
            User user = userRepository.findByAccountId(userId);

            boolean exists = userRepository.existsByAccountId(userId);
            if (!exists) {
                return GetMyProfileResponseDto.userNotFound();
            }

            return GetMyProfileResponseDto.success(user);

        } catch (Exception e) {
            e.printStackTrace();
            return GetMyProfileResponseDto.databaseError();
        }
    }
    @Override
    public ResponseEntity<? super GetOtherProfileResponseDto> getOtherMypage(Long userId) {
        try {
            // 현재 로그인한 사용자 ID 가져오기
            Long myId = jwtExtractProvider.findIdFromJwt();

            // 조회 대상 사용자 정보 가져오기
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            // 조회 대상 사용자가 존재하는지 확인
            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                return GetOtherProfileResponseDto.userNotFound();
            }

            // 현재 로그인한 사용자가 조회 대상 사용자를 찜했는지 확인
            boolean isMyHeartUser = heartRepository.existsByMyIdAndHeartedId(myId, userId);

            // 응답 생성
            return GetOtherProfileResponseDto.success(user, isMyHeartUser);

        } catch (Exception e) {
            e.printStackTrace();
            return GetOtherProfileResponseDto.databaseError();
        }
    }


    @Override
    @Transactional
    public ResponseEntity<? super EditMyProfileResponseDto> editMyProfile(EditMyProfileInfo info) {
        try {
            Long userId = jwtExtractProvider.findIdFromJwt();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                return EditMyProfileResponseDto.editFailed();
            }
            user.updateProfile(info);


        } catch (Exception e) {
            log.info("프로필 수정 실패: {}", e);
            return EditMyProfileResponseDto.databaseError();
        }
        return EditMyProfileResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super HeartingResponseDto> hearting(HeartingRequestDto dto) {
        try {
            Long heartedId = dto.getHeartedId();

            // 대상 사용자가 존재하는지 확인
            boolean exists = userRepository.existsById(heartedId);
            if (!exists) {
                return HeartingResponseDto.heartedIdNotFound();
            }

            // 현재 사용자 ID 가져오기
            Long userId = jwtExtractProvider.findIdFromJwt();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            // DB에서 myId와 heartedId로 Heart 레코드 확인
            Optional<Heart> existingHeart = heartRepository.findByMyIdAndHeartedId(user.getId(), heartedId);

            if (existingHeart.isPresent()) {
                // 찜하기 해제 (DB에서 삭제)
                heartRepository.delete(existingHeart.get());
                return HeartingResponseDto.success(); // 찜하기 해제 성공 응답
            }

            // 찜하기 진행 (DB에 저장)
            Heart heart = Heart.builder()
                    .myId(user.getId())
                    .heartedId(heartedId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            heartRepository.save(heart);

            return HeartingResponseDto.success(); // 찜하기 성공 응답

        } catch (Exception e) {
            log.info("찜하기 실패: {}", e);
            return HeartingResponseDto.databaseError(); // 데이터베이스 오류 응답
        }
    }
    @Override
    public ResponseEntity<? super GetHeartingListResponseDto> getHeartedList() {
        try {
            Long userId = jwtExtractProvider.findIdFromJwt();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            List<Heart> heartList = heartRepository.findAllByMyId(user.getId());

            return GetHeartingListResponseDto.success(heartList);

        } catch (Exception e) {
            log.info("찜리스트 받아오기 실패: {}", e);
            return HeartingResponseDto.databaseError();
        }
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(SendRepasswordRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
            User user = userRepository.findByAccountId(accountId);

            String rePasswordNum = RePasswordProvider.generatePassword();

            boolean isSendSuccess = rePasswordEmailProvider.sendVerificationEmail(accountId, rePasswordNum);
            if (!isSendSuccess) return SendRepasswordResponseDto.mailSendFail();

            String encodedRePasswordNum = passwordEncoder.encode(rePasswordNum);

            user.updatePassword(encodedRePasswordNum);

        } catch (Exception e) {
            log.info("임시비밀번호 재설정 실패: {}", e);
            return SendRepasswordResponseDto.databaseError();
        }
        return SendRepasswordResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super RepasswordResponseDto> repassword(RepasswordInfo info) {
        try {
            Long userId = jwtExtractProvider.findIdFromJwt();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            String currentPassword = info.getCurrentPassword();

            boolean isPasswordValid = passwordEncoder.matches(currentPassword, user.getPassword());
            if (!isPasswordValid) {
                return RepasswordResponseDto.wrongPassword();
            }
            String newPassword = info.getNewPassword();

            String encodedNewPassword = passwordEncoder.encode(newPassword);

            user.updatePassword(encodedNewPassword);

        } catch (Exception e) {
            log.info("비밀번호 재설정 실패: {}", e);
            return RepasswordResponseDto.databaseError();
        }
        return RepasswordResponseDto.success();
    }


    @Override
    @Transactional
    public ResponseEntity<? super UpdateLocationResponseDto> updateLocation(UpdateLocationInfo info) {
        try {
            Long userId = jwtExtractProvider.findIdFromJwt();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                return EditMyProfileResponseDto.editFailed();
            }
            String region = geocodingService.getRegionFromCoordinates(info.getLatitude(), info.getLongitude());

            user.updateLocation(info, region);

            return UpdateLocationResponseDto.success(region);
        } catch (Exception e) {
            log.info("위치업데이트 실패: {}", e);
            return UpdateLocationResponseDto.wrongLocation();
        }
    }


    @Override
    @Transactional
    public ResponseEntity<? super UpdateRecommendationResponseDto> updateRecommend(UpdateRecommendationRequestDto dto) {
        try {
            Long userId = dto.getUserId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                return GetOtherProfileResponseDto.userNotFound();
            }
            Integer recommendationCount = user.getRecommendationCount() + 1;

            user.updateRecommendation(recommendationCount);

        } catch (Exception e) {
            log.info("추천수 업데이트 실패: {}", e);
            return UpdateRecommendationResponseDto.databaseError();
        }
        return UpdateRecommendationResponseDto.success();
    }


    @Override
    public GetMyUserDto receiverEmail(String accountId) {
        try {
            User user = userRepository.findByAccountId(accountId);
            return new GetMyUserDto(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GetMyUserDto();
    }

    public UserInfo selectUserInfo(String receiverEmail) {
        User otherUser = userRepository.findByAccountId(receiverEmail);
        return otherUser.of();
    }
}
