package de.akvsoft.adohdo.security

import de.akvsoft.adohdo.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*

class CustomUserPrincipal(
    val id: UUID,
    private val email: String,
    private val password: String,
    private val name: String
) : OAuth2User, UserDetails {

    override fun getName() = name
    override fun getUsername() = email
    override fun getPassword() = password
    override fun getAttributes() = null
    override fun getAuthorities() = emptyList<GrantedAuthority>()
}

fun CustomUserPrincipal(user: User): CustomUserPrincipal {
    return CustomUserPrincipal(
        user.id!!,
        user.email,
        user.password,
        user.name
    )
}
