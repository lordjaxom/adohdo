package de.akvsoft.adohdo.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    private val id: String,
    private val email: String,
    private val password: String,
    private val attributes: Map<String, Any>
) : OAuth2User, UserDetails {

    override fun getName() = id
    override fun getUsername() = email
    override fun getPassword() = password
    override fun getAttributes() = attributes
    override fun getAuthorities() = emptyList<GrantedAuthority>()
}
