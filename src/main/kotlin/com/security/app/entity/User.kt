package com.security.app.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
data class User(
    @Id
    val id: String? = null,
    val firstname: String,
    val lastname: String,
    val email: String,
    val passcode: String,
    val role: Role,
    val enabled: Boolean,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?> = listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String = passcode

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = enabled

    override fun isAccountNonLocked(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = enabled

    override fun isEnabled(): Boolean = enabled
}
