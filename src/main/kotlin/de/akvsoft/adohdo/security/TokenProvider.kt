package de.akvsoft.adohdo.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.ZonedDateTime

@Service
class TokenProvider(
    @Value("\${adohdo.auth.token-secret}") private val tokenSecret: String,
    @Value("\${adohdo.auth.token-expiration-msec}") private val tokenExpirationMsec: Long,
) {

    companion object {
        val logger = LoggerFactory.getLogger(TokenProvider::class.java)!!
    }

    private val algorithm: Algorithm by lazy { Algorithm.HMAC256(tokenSecret) }

    fun createToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal

        val issuedAt = ZonedDateTime.now()
        val expiresAt = issuedAt.plus(Duration.ofMillis(tokenExpirationMsec))

        return JWT.create()
            .withSubject(userPrincipal.name)
            .withIssuedAt(issuedAt.toInstant())
            .withExpiresAt(expiresAt.toInstant())
            .sign(algorithm)
    }

    fun getUserIdFromToken(token: String): String {
        return JWT.require(algorithm).build().verify(token).subject
    }

    fun validateToken(token: String): Boolean {
        try {
            JWT.require(algorithm).build().verify(token)
            return true
        } catch (e: Exception) {
            logger.error("Invalid or expired JWT.")
        }
        return false
    }
}