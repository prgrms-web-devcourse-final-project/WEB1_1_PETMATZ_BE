package com.petmatz.domain.user.service;

import com.petmatz.api.user.request.*;
import com.petmatz.domain.user.info.*;
import com.petmatz.domain.user.response.*;
import com.petmatz.user.common.LogInResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationInfo info);
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpInfo info);
    ResponseEntity<? super SignInResponseDto> signIn(SignInInfo info, HttpServletResponse response);
    ResponseEntity<? super LogInResponseDto> logout(HttpServletResponse response);
    ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto);
    ResponseEntity<? super GetMyProfileResponseDto> getMypage();
    ResponseEntity<? super GetOtherProfileResponseDto> getOtherMypage(Long userId);
    ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(SendRepasswordRequestDto dto);
    ResponseEntity<? super RepasswordResponseDto> repassword(RepasswordInfo info);
    ResponseEntity<? super EditMyProfileResponseDto> editMyProfile(EditMyProfileInfo info);

    ResponseEntity<? super HeartingResponseDto> hearting(HeartingRequestDto dto);

    ResponseEntity<? super GetHeartingListResponseDto> getHeartedList();

    ResponseEntity<? super UpdateLocationResponseDto> updateLocation(UpdateLocationInfo info);

    ResponseEntity<? super UpdateRecommendationResponseDto> updateRecommend(UpdateRecommendationRequestDto dto);
<<<<<<< HEAD

    String findByUserEmail(Long userId);

=======
>>>>>>> 277c886bc6aec52fd29c8ff93a6ffa40558f2765
    GetMyUserDto receiverEmail(String accountId);
    ResponseEntity<? super EditKakaoProfileResponseDto> editKakaoProfile(EditKakaoProfileInfo of);
}
