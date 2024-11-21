package com.petmatz.user.service;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.entity.Certification;
import com.petmatz.user.entity.User;
import com.petmatz.user.provider.*;
import com.petmatz.user.repository.CertificationRepository;
import com.petmatz.user.repository.UserRepository;
import com.petmatz.user.request.*;
import com.petmatz.user.response.*;
import com.petmatz.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;
    private final RePasswordEmailProvider rePasswordEmailProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
            String certificationNumber = dto.getCertificationNumber();

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
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
            String certificationNumber = dto.getCertificationNumber();

            // 인증 번호 확인
            Certification Certification = certificationRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);
            boolean isMatched = Certification.getAccountId().equals(accountId) &&
                    Certification.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return SignUpResponseDto.certificationFail();

            // 비밀번호 암호화 후 저장
            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            User user = User.builder()
                    .accountId(dto.getAccountId())
                    .password(encodedPassword) // 비밀번호를 암호화 후 저장
                    .nickname(dto.getNickname())
                    .loginRole(User.LoginRole.ROLE_USER)
                    .gender(dto.getGender())
                    .preferredSize(dto.getPreferredSize())
                    .introduction(dto.getIntroduction())
                    .isCareAvailable(dto.getIsCareAvailable())
                    .role(User.Role.Dol)
                    .loginType(User.LoginType.Normal)
                    .isRegistered(false)
                    .recommendationCount(0)
                    .careCompletionCount(0)
                    .isDeleted(false)
                    .timeWage(dto.getTimeWage())
                    .monthWage(dto.getMonthWage())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);

            // 인증 엔티티 삭제
            certificationRepository.deleteByAccountId(accountId);
        } catch (Exception e) {
            log.info("회원 가입 실패: {}", e);
            return LogInResponseDto.databaseError();
        }
        return SignUpResponseDto.success();  // 회원 가입 성공 응답
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response) {
        try {
            String accountId = dto.getAccountId();
            User user = userRepository.findByAccountId(accountId);

            // 사용자 존재 여부 확인
            if (user == null) {
                log.info("사용자 조회 실패: {}", accountId);
                return SignInResponseDto.signInFail();
            }

            // 비밀번호 확인
            String password = dto.getPassword();
            String encodedPassword = user.getPassword();
            if (!passwordEncoder.matches(password, encodedPassword)) {
                log.info("비밀번호 불일치: {}", accountId);
                return SignInResponseDto.signInFail();
            }

            // JWT 생성
            String token = jwtProvider.create(accountId, user.getLoginRole());
            log.info("JWT 생성 완료: {}", token);

            // JWT 쿠키에 저장
            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);  // XSS 방지
            jwtCookie.setSecure(true);   // HTTPS만 허용
            jwtCookie.setPath("/");      // 모든 경로에서 접근 가능
            jwtCookie.setMaxAge(3600);   // 1시간 유효기간
            response.addCookie(jwtCookie);

            // 로그인 성공 응답 반환
            return SignInResponseDto.success(token, user.getLoginRole());
        } catch (Exception e) {
            log.error("로그인 처리 중 예외 발생", e);
            return SignInResponseDto.signInFail();
        }
    }

    @Override
    public ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto) {
        try {
            String accountId = findAccountIdFromJwt();
            User user = userRepository.findByAccountId(accountId);
            if (user == null) return DeleteIdResponseDto.idNotFound();  // 사용자 조회 실패

            // 비밀번호 일치 확인
            String password = dto.getPassword();
            String encodedPassword = user.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return DeleteIdResponseDto.idNotMatching();  // 비밀번호 불일치

            User updatedUser = user.builder()
                    .id(user.getId())
                    .accountId(user.getAccountId() + "-deleted")
                    .password("deleted-password")
                    .email(user.getEmail() + "-deleted")
                    .nickname(null)
                    .profileImg(null)
                    .loginRole(null)
                    .role(null)
                    .loginType(null)
                    .gender(null)
                    .preferredSize(user.getPreferredSize())
                    .introduction(null)
                    .isCareAvailable(user.getIsCareAvailable())
                    .isDeleted(true)
                    .timeWage(user.getTimeWage())
                    .monthWage(user.getMonthWage())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(updatedUser);
            certificationRepository.deleteByAccountId(accountId);

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
            String accountId = findAccountIdFromJwt();
            User user = userRepository.findByAccountId(accountId);

            boolean exists = userRepository.existsByAccountId(accountId);
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
    public ResponseEntity<? super GetMyProfileResponseDto> getOtherMypage(GetMyProfileRequestDto dto) {
        try {
            Long userId = dto.getUserId();
            User user = userRepository.findById(userId);

            boolean exists = userRepository.existsById(userId);
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
    public ResponseEntity<? super EditMyProfileResponseDto> editMyProfile(EditMyProfileRequestDto dto) {
        try {
            String accountId = findAccountIdFromJwt();
            User user = userRepository.findByAccountId(accountId);

            boolean exists = userRepository.existsByAccountId(accountId);
            if (!exists) {
                return EditMyProfileResponseDto.editFailed();
            }

            User updatedUser = user.builder()
                    .id(user.getId())
                    .accountId(user.getAccountId())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .nickname(dto.getNickname()) // 닉네임 업데이트
                    .profileImg(user.getProfileImg())
                    .loginRole(user.getLoginRole())
                    .role(user.getRole())
                    .loginType(user.getLoginType())
                    .gender(user.getGender())
                    .preferredSize(dto.getPreferredSize()) // 선호 크기 업데이트
                    .introduction(dto.getIntroduction()) // 자기소개 업데이트
                    .isCareAvailable(dto.isCareAvailable()) // 돌봄 가능 여부 업데이트
                    .isRegistered(user.getIsRegistered())
                    .recommendationCount(user.getRecommendationCount())
                    .careCompletionCount(user.getCareCompletionCount())
                    .latitude(user.getLatitude())
                    .longitude(user.getLongitude())
                    .isDeleted(false)
                    .timeWage(dto.getTimeWage()) // 시간당 임금 업데이트
                    .monthWage(dto.getMonthWage()) // 월 임금 업데이트
                    .createdAt(user.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(updatedUser);

        }catch (Exception e) {
            log.info("프로필 수정 실패: {}", e);
            return EditMyProfileResponseDto.databaseError();
        }
        return EditMyProfileResponseDto.success();
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(SendRepasswordRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
            User user = userRepository.findByAccountId(accountId);

            String rePasswordNum = RePasswordProvider.generatePassword();

            boolean isSendSuccess = rePasswordEmailProvider.sendVerificationEmail(accountId, rePasswordNum);
            if (!isSendSuccess) return SendRepasswordResponseDto.mailSendFail();

            String encodedRePasswordNum = passwordEncoder.encode(rePasswordNum);

            User updatedUser = user.builder()
                    .id(user.getId())
                    .accountId(user.getAccountId())
                    .password(encodedRePasswordNum)
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .profileImg(user.getProfileImg())
                    .loginRole(user.getLoginRole())
                    .role(user.getRole())
                    .loginType(user.getLoginType())
                    .gender(user.getGender())
                    .preferredSize(user.getPreferredSize())
                    .introduction(user.getIntroduction())
                    .isCareAvailable(user.getIsCareAvailable())
                    .isRegistered(user.getIsRegistered())
                    .recommendationCount(user.getRecommendationCount())
                    .careCompletionCount(user.getCareCompletionCount())
                    .latitude(user.getLatitude())
                    .longitude(user.getLongitude())
                    .isDeleted(false)
                    .timeWage(user.getTimeWage())
                    .monthWage(user.getMonthWage())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(updatedUser);

        } catch (Exception e) {
            log.info("임시비밀번호 재설정 실패: {}", e);
            return SendRepasswordResponseDto.databaseError();
        }
        return SendRepasswordResponseDto.success();
    }

    @Override
    public ResponseEntity<? super RepasswordResponseDto> repassword(RepasswordRequestDto dto) {
        try {
            String accountId = findAccountIdFromJwt();
            User user = userRepository.findByAccountId(accountId);

            String currentPassword = dto.getCurrentPassword();

            boolean isPasswordValid = passwordEncoder.matches(currentPassword, user.getPassword());
            if (!isPasswordValid) {
                return RepasswordResponseDto.wrongPassword();
            }
            String newPassword = dto.getNewPassword();

            String encodedNewPassword = passwordEncoder.encode(newPassword);

            User updatedUser = user.builder()
                    .id(user.getId())
                    .accountId(user.getAccountId())
                    .password(encodedNewPassword) //비밀번호 업데이트
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .profileImg(user.getProfileImg())
                    .loginRole(user.getLoginRole())
                    .role(user.getRole())
                    .loginType(user.getLoginType())
                    .gender(user.getGender())
                    .preferredSize(user.getPreferredSize())
                    .introduction(user.getIntroduction())
                    .isCareAvailable(user.getIsCareAvailable())
                    .isRegistered(user.getIsRegistered())
                    .recommendationCount(user.getRecommendationCount())
                    .careCompletionCount(user.getCareCompletionCount())
                    .latitude(user.getLatitude())
                    .longitude(user.getLongitude())
                    .isDeleted(false)
                    .timeWage(user.getTimeWage())
                    .monthWage(user.getMonthWage())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(updatedUser);
        } catch (Exception e) {
            log.info("비밀번호 재설정 실패: {}", e);
            return RepasswordResponseDto.databaseError();
        }
        return RepasswordResponseDto.success();
    }



    private String findAccountIdFromJwt() {
        String accountId = "Default_Id";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            accountId = authentication.getName();
        }
        return accountId;
    }
}
