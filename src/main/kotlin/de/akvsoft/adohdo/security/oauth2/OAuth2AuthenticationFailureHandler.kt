package de.akvsoft.adohdo.security.oauth2

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder


@Component
class OAuth2AuthenticationFailureHandler(
    private val oauth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        val redirectUrl = oauth2AuthorizationRequestRepository.findAuthorizationRequestRedirectUri(request) ?: "/"
        val targetUrl = UriComponentsBuilder
            .fromUriString(redirectUrl)
            .queryParam("error", exception.localizedMessage)
            .build().toUriString()

        oauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}