package de.akvsoft.adohdo.register

import de.akvsoft.adohdo.user.User
import de.akvsoft.adohdo.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegisterService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(request: RegisterRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw UserExistsException()
        }

        User(request.email, passwordEncoder.encode(request.password), request.name)
            .also { userRepository.save(it) }
    }
}