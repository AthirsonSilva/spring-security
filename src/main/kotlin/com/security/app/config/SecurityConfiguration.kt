package com.security.app.config

import com.security.app.security.JwtAuthenticationFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    val jwtAuthenticationFilter: JwtAuthenticationFilter,
    val authenticationProvider: AuthenticationProvider,
) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
        http.authorizeHttpRequests { request ->
            request
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
        }
        http.authenticationProvider(authenticationProvider)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.exceptionHandling { exception: ExceptionHandlingConfigurer<HttpSecurity?> ->
            exception
                .authenticationEntryPoint { _: HttpServletRequest?, response: HttpServletResponse, ex: AuthenticationException ->
                    response
                        .sendError(HttpStatus.UNAUTHORIZED.value(), ex.message)
                }
        }
        return http.build()
    }
}
