package com.security.app.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService {
    fun extractUsername(token: String?): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    fun generateToken(
        extractClaims: Map<String?, Any?>?,
        userDetails: UserDetails
    ): String {
        return Jwts
            .builder()
            .setClaims(extractClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(signInKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)

        return claimsResolver.apply(claims)
    }

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)

        return (username == userDetails.username && !isTokenExpired(token))
    }

    fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun extractExpiration(token: String?): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(signInKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private val signInKey: Key
        get() {
            val keyBytes = Decoders.BASE64.decode(SECRET_KEY)

            return Keys.hmacShaKeyFor(keyBytes)
        }

    companion object {
        private val SECRET_KEY: String = System.getenv("SECRET_KEY")
    }
}
