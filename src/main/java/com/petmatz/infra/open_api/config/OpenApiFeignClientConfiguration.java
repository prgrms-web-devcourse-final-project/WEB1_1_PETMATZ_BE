package com.petmatz.infra.open_api.config;

import org.springframework.context.annotation.Bean;

public class OpenApiFeignClientConfiguration {

    @Bean
    public OpenApiRequestInterceptor openApiRequestInterceptor() {
        return new OpenApiRequestInterceptor();
    }

}
