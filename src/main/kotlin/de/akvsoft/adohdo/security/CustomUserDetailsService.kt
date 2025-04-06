package de.akvsoft.adohdo.security

import de.akvsoft.adohdo.user.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String) =
        userRepository
            .findByEmail(email)
            ?.let { CustomUserPrincipal(it) }
            ?: throw UsernameNotFoundException("User with email $email not found")

    fun loadUserById(id: UUID) =
        userRepository
            .findById(id)
            .getOrNull()
            ?.let { CustomUserPrincipal(it) }
            ?: throw UsernameNotFoundException("User with id $id not found")
}