package com.petmatz.infra.open_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenApiResponse<T>(@JsonProperty("response") Response<T> response) {

    public record Response<T>(
            @JsonProperty("header") Header header, @JsonProperty("body") Body<T> body) {

        public record Header(
                @JsonProperty("reqNo") String reqNo,
                @JsonProperty("resultCode") String resultCode,
                @JsonProperty("resultMsg") String resultMessage) {
        }

        public record Body<T>(
                @JsonProperty("item") T item ) {}
    }

    public T getItem() {
        return response.body.item;
    }
}
