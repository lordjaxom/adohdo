package de.akvsoft.adohdo.security.oauth2

import de.akvsoft.adohdo.security.TokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.BadRequestException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenProvider: TokenProvider,
    private val oauth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): String {
        val redirectUri = oauth2AuthorizationRequestRepository.findAuthorizationRequestRedirectUri(request)
        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
        }

        val targetUrl = redirectUri ?: defaultTargetUrl
        val token = tokenProvider.createToken(authentication)
        return UriComponentsBuilder
            .fromUriString(targetUrl)
            .queryParam("token", token)
            .build()
            .toUriString()
    }

    private fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        oauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {

        // TODO
        return true
//        val clientRedirectUri = URI.create(uri)
//
//        return appProperties.getOAuth2().getAuthorizedRedirectUris()
//            .stream()
//            .anyMatch { authorizedRedirectUri ->
//                // Only validate host and port. Let the clients use different paths if they want to
//                val authorizedURI: URI = URI.create(authorizedRedirectUri)
//                authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                        && authorizedURI.getPort() === clientRedirectUri.getPort()
//            }
    }
}