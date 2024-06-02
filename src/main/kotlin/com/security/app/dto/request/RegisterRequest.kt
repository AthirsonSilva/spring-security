package com.security.app.dto.request

data class RegisterRequest (
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
)
