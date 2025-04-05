package de.akvsoft.adohdo.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class TokenAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = getJWTFromRequest(request)
            if (!jwt.isNullOrEmpty() && tokenProvider.validateToken(jwt)) {
                val userId = tokenProvider.getUserIdFromToken(jwt)
                val userDetails: UserDetails = userDetailsService.loadUserById(userId)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            logger.error("Could not set user authentication in security context.", ex)
        }

        filterChain.doFilter(request, response)
    }

    private fun getJWTFromRequest(request: HttpServletRequest): String? {
        return request
            .getHeader(HttpHeaders.AUTHORIZATION)
            ?.run { if (isNotEmpty() && startsWith("Bearer ")) substring(7) else null }
    }
}