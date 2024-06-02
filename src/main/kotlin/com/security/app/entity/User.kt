package com.security.app.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
data class User(
    val id: String? = null,
    val firstname: String,
    val lastname: String,
    val email: String,
    val passcode: String,
    val role: Role,
    val enabled: Boolean,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return passcode
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return enabled
    }

    override fun isAccountNonLocked(): Boolean {
        return enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return enabled
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}
