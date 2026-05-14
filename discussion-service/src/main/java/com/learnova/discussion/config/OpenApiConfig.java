package com.learnova.discussion.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI discussionServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Discussion Service API")
                        .description("Learnova Discussion Service APIs")
                        .version("1.0"));
    }
}