package com.petmatz.user.service;

import com.petmatz.user.request.*;
import com.petmatz.user.response.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
    ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto);
    ResponseEntity<? super GetMypageResponseDto> getMypage(GetMypageRequestDto dto);
    ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(@Valid SendRepasswordRequestDto requestBody);
    ResponseEntity<? super RepasswordResponseDto> repassword(@Valid RepasswordRequestDto requestBody);
}
