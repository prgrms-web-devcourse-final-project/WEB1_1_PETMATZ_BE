package com.petmatz.user.service;

import com.petmatz.user.common.LogInResponseDto;
import com.petmatz.user.common.Role;
import com.petmatz.user.entity.CertificationEntity;
import com.petmatz.user.entity.UserEntity;
import com.petmatz.user.provider.CertificationNumberProvider;
import com.petmatz.user.provider.EmailProvider;
import com.petmatz.user.provider.JwtProvider;
import com.petmatz.user.repository.CertificationRepository;
import com.petmatz.user.repository.UserRepository;
import com.petmatz.user.request.*;
import com.petmatz.user.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;
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
            CertificationEntity certificationEntity = CertificationEntity.builder().accountId(accountId).certificationNumber(certificationNumber).build();
            certificationRepository.save(certificationEntity);

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
            CertificationEntity certificationEntity = certificationRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);
            if (certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

            // 이메일 및 인증 번호가 일치하는지 확인
            boolean isMatch = certificationEntity.getAccountId().equals(accountId) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatch) return CheckCertificationResponseDto.certificationFail();

        } catch (Exception e) {
            log.info("인증 번호 확인 실패: {}", e);
            return LogInResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();  // 인증 성공 응답
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
            String certificationNumber = dto.getCertificationNumber();

            // 인증 번호 확인
            CertificationEntity certificationEntity = certificationRepository.findByAccountId(accountId);
            boolean isMatched = certificationEntity.getAccountId().equals(accountId) &&
                    certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return SignUpResponseDto.certificationFail();

            // 비밀번호 암호화 후 저장
            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity = UserEntity.builder()
                    .accountId(dto.getAccountId())
                    .password(dto.getPassword()) // Make sure to hash the password before saving
                    .nickname(dto.getNickname())
                    .loginRole(Role.USER)
                    .gender(UserEntity.Gender.valueOf(dto.getGender())) // Convert string to Enum
                    .preferredSize(UserEntity.PreferredSize.valueOf(dto.getPreferredSize())) // Convert string to Enum
                    .introduction(dto.getIntroduction())
                    .isCareAvailable(dto.getIsCareAvailable())
                    .role(UserEntity.Role.Dol) // Assign a default role or map appropriately as needed
                    .loginType(UserEntity.LoginType.Normal) // Default value or based on logic
                    .isRegistered(false) // You can set default values or change based on your logic
                    .recommendationCount(0) // Default value
                    .careCompletionCount(0) // Default value
                    .build();
            userRepository.save(userEntity);

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

        try {
            String accountId = dto.getAccountId();
            UserEntity userEntity = userRepository.findByAccountId(accountId);
            if (userEntity == null) return SignInResponseDto.signInFail();  // 사용자 조회 실패

            // 비밀번호 일치 확인
            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();

            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return SignInResponseDto.signInFail();  // 비밀번호 불일치

            // JWT 토큰 생성
            token = jwtProvider.create(accountId);

            return SignInResponseDto.success(token);  // 로그인 성공 및 토큰 반환
        } catch (Exception e) {
            log.info("로그인 실패: {}", e);
            return SignInResponseDto.signInFail();
        }
    }

    @Override
    public ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto) {
        try {
            String accountId = dto.getAccountId();
            UserEntity userEntity = userRepository.findByAccountId(accountId);
            if (userEntity == null) return DeleteIdResponseDto.idNotFound();  // 사용자 조회 실패

            // 비밀번호 일치 확인
            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return DeleteIdResponseDto.idNotMatching();  // 비밀번호 불일치

            UserEntity updatedUserEntity = UserEntity.builder()
                    .id(userEntity.getId())
                    .accountId(userEntity.getAccountId() + "-deleted")
                    .password("deleted-password")
                    .email(userEntity.getEmail() + "-deleted")
                    .nickname(userEntity.getNickname()) // Preserving existing values
                    .profileImg(userEntity.getProfileImg()) // Preserving existing values
                    .loginRole(userEntity.getLoginRole()) // Preserving existing values
                    .role(userEntity.getRole()) // Preserving existing values
                    .loginType(userEntity.getLoginType()) // Preserving existing values
                    .gender(userEntity.getGender()) // Preserving existing values
                    .preferredSize(userEntity.getPreferredSize()) // Preserving existing values
                    .introduction(userEntity.getIntroduction()) // Preserving existing values
                    .isCareAvailable(userEntity.getIsCareAvailable()) // Preserving existing values
                    .isRegistered(userEntity.getIsRegistered()) // Preserving existing values
                    .recommendationCount(userEntity.getRecommendationCount()) // Preserving existing values
                    .careCompletionCount(userEntity.getCareCompletionCount()) // Preserving existing values
                    .latitude(userEntity.getLatitude()) // Preserving existing values
                    .longitude(userEntity.getLongitude()) // Preserving existing values
                    .isDeleted(true) // Soft delete flag
                    .createdAt(userEntity.getCreatedAt()) // Preserving original createdAt value
                    .updatedAt(LocalDateTime.now()) // Updating the updatedAt timestamp
                    .build();

            userRepository.save(updatedUserEntity);
            certificationRepository.deleteByAccountId(accountId);

        } catch (Exception e) {
            log.info("회원 삭제 실패: {}", e);
            return DeleteIdResponseDto.databaseError();  // 데이터베이스 오류 처리
        }
        return DeleteIdResponseDto.success();  // 삭제 성공 응답
    }

}
