package com.petmatz.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO JWT 사용시 아래의 주석 해제 필요
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
//        String jwt = "JWT";
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
//        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
//                .name(jwt)
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//        );
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
//                .addSecurityItem(securityRequirement)
//                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("PetMetz API Test") // API의 제목
                .description("TEAM 11의 Final Project의 API 명세서  입니다.") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
