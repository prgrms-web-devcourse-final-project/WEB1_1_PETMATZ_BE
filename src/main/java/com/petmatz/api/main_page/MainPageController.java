package com.petmatz.api.main_page;

import com.petmatz.api.global.dto.Response;
import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.sosboard.SosBoard;
import com.petmatz.domain.sosboard.SosBoardServiceImpl;
import com.petmatz.domain.sosboard.dto.PageResponseDto;
import com.petmatz.domain.sosboard.dto.SosBoardServiceDto;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/main")
public class MainPageController {

    private final SosBoardServiceImpl sosBoardServiceImpl;
    private final UserRepository userRepository;
    private final JwtExtractProvider jwtExtractProvider;

    public void getUserSchedule() {
    }

    @GetMapping("/SOSpage")
    public Response<?> getSOSpage() {
        Long userUUID = jwtExtractProvider.findIdFromJwt();
        Optional<User> byId = userRepository.findById(userUUID);
        User user = byId.get();
        PageResponseDto<SosBoardServiceDto> allSosBoards = sosBoardServiceImpl.getAllSosBoards(user.getRegion(), 0, 100);
        return Response.success(allSosBoards);
    }

}
