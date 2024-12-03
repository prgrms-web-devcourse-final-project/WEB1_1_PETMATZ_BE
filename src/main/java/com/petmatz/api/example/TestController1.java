package com.petmatz.api.example;

import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.aws.AwsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController1 {

    private final AwsClient awsS3Service;

    @PostMapping("/tset2")
    public Response<?> chatPage1(@RequestBody TestRequest base64Image) {
        String userName = "테스트_유저";
        String fileName = "강아지_사진";
        String imageURL = awsS3Service.uploadImg(base64Image.base64Image(), userName, fileName);

        return Response.success(imageURL);
    }
}
