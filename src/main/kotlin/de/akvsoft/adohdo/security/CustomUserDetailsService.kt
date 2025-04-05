package de.akvsoft.adohdo.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        return UserPrincipal("", email, "", mapOf())
    }

    fun loadUserById(id: String): UserDetails {
        TODO("NOT IMPLEMENTED YET")
    }
}