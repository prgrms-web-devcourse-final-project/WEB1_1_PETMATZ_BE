package com.petmatz.user.service;

import com.petmatz.user.request.*;
import com.petmatz.user.response.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response);
    ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto);
    ResponseEntity<? super GetMyProfileResponseDto> getMypage();
    ResponseEntity<? super GetMyProfileResponseDto> getOtherMypage(GetMyProfileRequestDto dto);
    ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(SendRepasswordRequestDto dto);
    ResponseEntity<? super RepasswordResponseDto> repassword(RepasswordRequestDto dto);
    ResponseEntity<? super EditMyProfileResponseDto> editMyProfile(@Valid EditMyProfileRequestDto dto);
}
