package com.petmatz.infra.firebase.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FcmMessage {

    private String token;
    private String title;
    private String body;

    // 모든 필드를 초기화하는 생성자
    @JsonCreator
    public FcmMessage(@JsonProperty("token") String token,
                      @JsonProperty("title") String title,
                      @JsonProperty("body") String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }

    // 정적 팩토리 메서드
    public static FcmMessage of(@JsonProperty("token") String token,
                                @JsonProperty("title") String title,
                                @JsonProperty("body") String body) {
        return new FcmMessage(token, title, body);
    }
}
