package com.security.app.dto.request

data class AuthenticationRequest (
    val email: String,
    val password: String,
)
