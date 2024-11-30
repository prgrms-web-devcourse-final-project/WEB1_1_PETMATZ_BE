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
            Certification Certification = certificationRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);
            boolean isMatched = Certification.getAccountId().equals(accountId) &&
                    Certification.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return SignUpResponseDto.certificationFail();

            // 비밀번호 암호화 후 저장
            String password = info.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            info.setPassword(encodedPassword);

            String region = geocodingService.getRegionFromCoordinates(info.getLatitude(), info.getLongitude());
            User user = UserFactory.createNewUser(info, encodedPassword, region);
            userRepository.save(user);

            // 인증 엔티티 삭제
            certificationRepository.deleteAllByAccountId(accountId);
        } catch (Exception e) {
            log.info("회원 가입 실패: {}", e);
            return LogInResponseDto.databaseError();
        }
        return SignUpResponseDto.success();  // 회원 가입 성공 응답
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInInfo info, HttpServletResponse response) {
        try {
            String accountId = info.getAccountId();
            User user = userRepository.findByAccountId(accountId);

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

            // JWT 쿠키에 저장
            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);  // XSS 방지
            jwtCookie.setSecure(true);   // HTTPS만 허용
            jwtCookie.setPath("/");      // 모든 경로에서 접근 가능
            jwtCookie.setMaxAge(3600);   // 1시간 유효기간
            response.addCookie(jwtCookie);

            // 로그인 성공 응답 반환
            return SignInResponseDto.success(user); // User 객체 전달
        } catch (Exception e) {
            log.error("로그인 처리 중 예외 발생", e);
            return SignInResponseDto.signInFail();
        }
    }

    @Override
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

//            User updatedUser = UserFactory.createDeletedUser(user);
//            userRepository.save(updatedUser);

            certificationRepository.deleteById(userId);
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
            Optional<User> user = userRepository.findById(userId);

            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                return GetOtherProfileResponseDto.userNotFound();
            }

            return GetOtherProfileResponseDto.success(user.orElse(null));

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

//            User updatedUser = UserFactory.createUpdatedUser(user, info);
//            userRepository.save(updatedUser);

        } catch (Exception e) {
            log.info("프로필 수정 실패: {}", e);
            return EditMyProfileResponseDto.databaseError();
        }
        return EditMyProfileResponseDto.success();
    }

    @Override
    public ResponseEntity<? super HeartingResponseDto> hearting(HeartingRequestDto dto) {
        try {
            Long heartedId = dto.getHeartedId();

            boolean exists = userRepository.existsById(heartedId);
            if (!exists) {
                return HeartingResponseDto.heartedIdNotFound();
            }

            Long userId = jwtExtractProvider.findIdFromJwt();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

            Heart heart = Heart.builder()
                    .myId(user.getId())
                    .heartedId(heartedId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            heartRepository.save(heart);


        } catch (Exception e) {
            log.info("찜하기 실패: {}", e);
            return HeartingResponseDto.databaseError();
        }
        return HeartingResponseDto.success();
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
//            User updatedUser = UserFactory.createUpdatedPasswordUser(user, encodedRePasswordNum);
//            userRepository.save(updatedUser);

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

//            User updatedUser = UserFactory.createUpdatedPasswordUser(user, encodedNewPassword);
//            userRepository.save(updatedUser);

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

//            User updatedUser = UserFactory.createLocationUpdateUser(user, info, region);
//            userRepository.save(updatedUser);

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

//            User updatedUser = UserFactory.createRecommendationUpdateUser(user, recommendationCount);
//            userRepository.save(updatedUser);

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

    public void selectUserInfo(String receiverEmail) {
        User byAccountId = userRepository.findByAccountId(receiverEmail);
//        return User.of
    }
}
