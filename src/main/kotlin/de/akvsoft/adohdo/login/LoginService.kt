package de.akvsoft.adohdo.login

import de.akvsoft.adohdo.security.TokenProvider
import de.akvsoft.adohdo.user.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger { }

@Service
class LoginService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) {

    fun login(request: LoginRequest): LoginResponse {
        logger.info { "Local login requested: $request" }

        val user = userRepository.findByEmail(request.email) ?: throw UsernameNotFoundException(request.email)

        // TODO: registration
        if (user.password.isEmpty()) {
            logger.info { "Saving password for ${user.email}" }
            user.password = passwordEncoder.encode(request.password)
            userRepository.saveAndFlush(user)
        }

        return authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
            .let { tokenProvider.createToken(it) }
            .let { LoginResponse(it) }
    }
}