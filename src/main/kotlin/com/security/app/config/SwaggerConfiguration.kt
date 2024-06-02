package com.security.app.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration {
    @Bean
    fun customOpenAPI(): OpenAPI {
        val openApi = OpenAPI()

        val securityScheme = SecurityScheme()
            .name("Authorization")
            .description("JWT Token")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)

        openApi.components(
            Components()
                .addSecuritySchemes("JWT Authentication", securityScheme)
        )
        return openApi
    }
}
