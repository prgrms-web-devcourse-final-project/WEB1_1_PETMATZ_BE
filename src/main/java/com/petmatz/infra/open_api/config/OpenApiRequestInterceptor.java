package com.petmatz.infra.open_api.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class OpenApiRequestInterceptor implements RequestInterceptor {

    @Value("${openapi.service.animal_number_key}")
    private String serviceKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-type", "application/json");
        template.query("serviceKey", serviceKey);
        template.query("_type", "json");
    }
}
