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
            //이메일 전송과 동시에 중복검사
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
            return LogInResponseDto.databaseError();
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
                    .password(dto.getPassword()) // Make sure to hash the password before saving
                    .nickname(dto.getNickname())
                    .loginRole(User.LoginRole.ROLE_USER)
                    .gender(dto.getGender()) // Convert string to Enum
                    .preferredSize(dto.getPreferredSize()) // Convert string to Enum
                    .introduction(dto.getIntroduction())
                    .isCareAvailable(dto.getIsCareAvailable())
                    .role(User.Role.Dol) // Assign a default role or map appropriately as needed
                    .loginType(User.LoginType.Normal) // Default value or based on logic
                    .isRegistered(false) // You can set default values or change based on your logic
                    .recommendationCount(0) // Default value
                    .careCompletionCount(0) // Default value
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
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        String token = null;
        User.LoginRole loginRole= null;

        try {
            String accountId = dto.getAccountId();
            User user = userRepository.findByAccountId(accountId);
            if (user == null) return SignInResponseDto.signInFail();  // 사용자 조회 실패

            // 비밀번호 일치 확인
            String password = dto.getPassword();
            String encodedPassword = user.getPassword();

            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return SignInResponseDto.signInFail();  // 비밀번호 불일치

            // JWT 토큰 생성
            token = jwtProvider.create(accountId, User.LoginRole.ROLE_USER);

            return SignInResponseDto.success(token, User.LoginRole.ROLE_USER);  // 로그인 성공 및 토큰 반환
        } catch (Exception e) {
            log.info("로그인 실패: {}", e);
            return SignInResponseDto.signInFail();
        }
    }

    @Override
    public ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
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
                    .nickname(user.getNickname()) // Preserving existing values
                    .profileImg(user.getProfileImg()) // Preserving existing values
                    .loginRole(user.getLoginRole()) // Preserving existing values
                    .role(user.getRole()) // Preserving existing values
                    .loginType(user.getLoginType()) // Preserving existing values
                    .gender(user.getGender()) // Preserving existing values
                    .preferredSize(user.getPreferredSize()) // Preserving existing values
                    .introduction(user.getIntroduction()) // Preserving existing values
                    .isCareAvailable(user.getIsCareAvailable()) // Preserving existing values
                    .isRegistered(user.getIsRegistered()) // Preserving existing values
                    .recommendationCount(user.getRecommendationCount()) // Preserving existing values
                    .careCompletionCount(user.getCareCompletionCount()) // Preserving existing values
                    .latitude(user.getLatitude()) // Preserving existing values
                    .longitude(user.getLongitude()) // Preserving existing values
                    .isDeleted(true) // Soft delete flag
                    .timeWage(user.getTimeWage()) // Preserving existing values
                    .monthWage(user.getMonthWage()) // Preserving existing values
                    .createdAt(user.getCreatedAt()) // Preserving original createdAt value
                    .updatedAt(LocalDateTime.now()) // Updating the updatedAt timestamp
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
    public ResponseEntity<? super GetMypageResponseDto> getMypage(GetMypageRequestDto dto) {
        try{
            String accountId=dto.getAccountId();
            System.out.println(accountId);

            boolean exists=userRepository.existsByAccountId(accountId);
            if (!exists) {
                return GetMypageResponseDto.userNotFound();
            }

            User user=userRepository.findByAccountId(accountId);

            return GetMypageResponseDto.success(user);

        }catch(Exception e){
            e.printStackTrace();
            return GetMypageResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(SendRepasswordRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
            User user = userRepository.findByAccountId(accountId);

            String rePasswordNum = RePasswordProvider.generatePassword();

            // Send the password reset email
            boolean isSendSuccess = rePasswordEmailProvider.sendVerificationEmail(accountId, rePasswordNum);
            if (!isSendSuccess) return SendRepasswordResponseDto.mailSendFail();

            // Encrypt the new password before saving
            String encodedRePasswordNum = passwordEncoder.encode(rePasswordNum);

            User updatedUser = user.builder()
                    .id(user.getId())
                    .accountId(user.getAccountId())
                    .password(encodedRePasswordNum) // Save the encrypted password
                    .email(user.getEmail())
                    .nickname(user.getNickname()) // Preserving existing values
                    .profileImg(user.getProfileImg()) // Preserving existing values
                    .loginRole(user.getLoginRole()) // Preserving existing values
                    .role(user.getRole()) // Preserving existing values
                    .loginType(user.getLoginType()) // Preserving existing values
                    .gender(user.getGender()) // Preserving existing values
                    .preferredSize(user.getPreferredSize()) // Preserving existing values
                    .introduction(user.getIntroduction()) // Preserving existing values
                    .isCareAvailable(user.getIsCareAvailable()) // Preserving existing values
                    .isRegistered(user.getIsRegistered()) // Preserving existing values
                    .recommendationCount(user.getRecommendationCount()) // Preserving existing values
                    .careCompletionCount(user.getCareCompletionCount()) // Preserving existing values
                    .latitude(user.getLatitude()) // Preserving existing values
                    .longitude(user.getLongitude()) // Preserving existing values
                    .isDeleted(false) // Soft delete flag
                    .timeWage(user.getTimeWage()) // Preserving existing values
                    .monthWage(user.getMonthWage()) // Preserving existing values
                    .createdAt(user.getCreatedAt()) // Preserving original createdAt value
                    .updatedAt(LocalDateTime.now()) // Updating the updatedAt timestamp
                    .build();

            userRepository.save(updatedUser);

        } catch (Exception e) {
            log.info("비밀번호 재설정 실패: {}", e);
            return SendRepasswordResponseDto.databaseError();
        }
        return SendRepasswordResponseDto.success();
    }

    @Override
    public ResponseEntity<? super RepasswordResponseDto> repassword(RepasswordRequestDto dto) {
        try{
            String accountId = findUserIdFromJwt();
            User user = userRepository.findByAccountId(accountId);

            String currentPassword=dto.getCurrentPassword();

            boolean isPasswordValid = passwordEncoder.matches(currentPassword, user.getPassword());
            if (!isPasswordValid) {
                return RepasswordResponseDto.wrongPassword();
            }
            String newPassword = dto.getNewPassword();

            String encodedNewPassword = passwordEncoder.encode(newPassword);

            User updatedUser = user.builder()
                    .id(user.getId())
                    .accountId(user.getAccountId())
                    .password(encodedNewPassword) // Save the encrypted password
                    .email(user.getEmail())
                    .nickname(user.getNickname()) // Preserving existing values
                    .profileImg(user.getProfileImg()) // Preserving existing values
                    .loginRole(user.getLoginRole()) // Preserving existing values
                    .role(user.getRole()) // Preserving existing values
                    .loginType(user.getLoginType()) // Preserving existing values
                    .gender(user.getGender()) // Preserving existing values
                    .preferredSize(user.getPreferredSize()) // Preserving existing values
                    .introduction(user.getIntroduction()) // Preserving existing values
                    .isCareAvailable(user.getIsCareAvailable()) // Preserving existing values
                    .isRegistered(user.getIsRegistered()) // Preserving existing values
                    .recommendationCount(user.getRecommendationCount()) // Preserving existing values
                    .careCompletionCount(user.getCareCompletionCount()) // Preserving existing values
                    .latitude(user.getLatitude()) // Preserving existing values
                    .longitude(user.getLongitude()) // Preserving existing values
                    .isDeleted(false) // Soft delete flag
                    .timeWage(user.getTimeWage()) // Preserving existing values
                    .monthWage(user.getMonthWage()) // Preserving existing values
                    .createdAt(user.getCreatedAt()) // Preserving original createdAt value
                    .updatedAt(LocalDateTime.now()) // Updating the updatedAt timestamp
                    .build();

            userRepository.save(updatedUser);
        }catch (Exception e) {
            log.info("비밀번호 재설정 실패: {}", e);
            return RepasswordResponseDto.databaseError();
        }
        return RepasswordResponseDto.success();
    }

    private String findUserIdFromJwt() {
        String userId = "Default user id...";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            userId = authentication.getName();
        }
        return userId;
    }
}
