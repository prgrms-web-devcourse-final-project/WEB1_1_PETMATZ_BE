package com.petmatz.domain.user.service;

import com.petmatz.api.user.request.DeleteIdRequestDto;
import com.petmatz.api.user.request.EmailCertificationRequestDto;
import com.petmatz.api.user.request.HeartingRequestDto;
import com.petmatz.api.user.request.SendRepasswordRequestDto;
import com.petmatz.domain.user.info.*;
import com.petmatz.domain.user.response.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationInfo info);
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpInfo info);
    ResponseEntity<? super SignInResponseDto> signIn(SignInInfo info, HttpServletResponse response);
    ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto);
    ResponseEntity<? super GetMyProfileResponseDto> getMypage();
    ResponseEntity<? super GetMyProfileResponseDto> getOtherMypage(Long userId);
    ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(SendRepasswordRequestDto dto);
    ResponseEntity<? super RepasswordResponseDto> repassword(RepasswordInfo info);
    ResponseEntity<? super EditMyProfileResponseDto> editMyProfile(EditMyProfileInfo info);

    ResponseEntity<? super HeartingResponseDto> hearting(HeartingRequestDto dto);

    ResponseEntity<? super GetHeartingListResponseDto> getHeartedList();
}
