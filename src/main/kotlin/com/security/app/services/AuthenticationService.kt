package com.security.app.services

import com.security.app.dto.request.AuthenticationRequest
import com.security.app.dto.request.RegisterRequest
import com.security.app.dto.response.AuthenticationResponse
import com.security.app.entity.Role
import com.security.app.entity.User
import com.security.app.repository.UserRepository
import com.security.app.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager,
) {

    fun register(request: RegisterRequest): AuthenticationResponse {
        val user = User(
            firstname = request.firstname,
            lastname = request.lastname,
            email = request.email,
            passcode = passwordEncoder.encode(request.password),
            role = Role.USER,
            enabled = true
        )

        userRepository.save(user)

        val jwtToken = jwtService.generateToken(user)

        return AuthenticationResponse(token = jwtToken)
    }

    fun login(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )

        val user = userRepository.findByEmail(request.email)
            .orElseThrow { AuthenticationServiceException((("User of email '${request.email}' not found"))) }

        val jwtToken = jwtService.generateToken(user!!)

        return AuthenticationResponse(token = jwtToken)
    }
}
