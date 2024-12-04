package com.petmatz.api.example;

import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.aws.AwsClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test1")
@RequiredArgsConstructor
public class tController {



    private final AwsClient awsClient;

    @PostMapping
    public Response<?> t(@RequestBody TestDTO test) {


        return Response.success(awsClient.uploadImg(test.getProfileImage(), test.getStandard()));
    }

}
