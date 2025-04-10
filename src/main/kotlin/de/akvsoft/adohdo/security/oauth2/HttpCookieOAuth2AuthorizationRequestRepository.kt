package de.akvsoft.adohdo.security.oauth2

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component

private const val OAUTH2_AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request"
private const val OAUTH2_REDIRECT_URI_COOKIE_NAME = "oauth2_redirect_uri"
private const val REDIRECT_URI_PARAM_NAME = "redirect_uri"
private const val COOKIE_EXPIRE_SECONDS = 180

@Component
class HttpCookieOAuth2AuthorizationRequestRepository(
    private val serializer: OAuth2AuthorizationRequestSerializer
) : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    override fun loadAuthorizationRequest(request: HttpServletRequest) =
        findCookieValue(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME)?.let { serializer.deserialize(it) }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response)
            return
        }

        saveCookieValue(response, OAUTH2_AUTH_REQUEST_COOKIE_NAME, serializer.serialize(authorizationRequest))
        saveCookieValue(response, OAUTH2_REDIRECT_URI_COOKIE_NAME, request.getParameter(REDIRECT_URI_PARAM_NAME))
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest, response: HttpServletResponse): OAuth2AuthorizationRequest? {
        val authorizationRequest = loadAuthorizationRequest(request)
        deleteCookies(request, response, OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        return authorizationRequest
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        deleteCookies(request, response, OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        deleteCookies(request, response, OAUTH2_REDIRECT_URI_COOKIE_NAME)
    }

    fun findAuthorizationRequestRedirectUri(request: HttpServletRequest) =
        findCookieValue(request, OAUTH2_REDIRECT_URI_COOKIE_NAME)

    private fun findCookieValue(request: HttpServletRequest, name: String) =
        request.cookies?.find { it.name == name }?.value

    private fun saveCookieValue(response: HttpServletResponse, name: String, value: String) {
        val cookie = Cookie(name, value).apply {
            path = "/"
            isHttpOnly = true
            maxAge = COOKIE_EXPIRE_SECONDS
        }
        response.addCookie(cookie)
    }

    private fun deleteCookies(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        request.cookies
            ?.filter { it.name == name }
            ?.forEach {
                it.apply {
                    value = ""
                    path = "/"
                    maxAge = 0
                }
                response.addCookie(it)
            }
    }
}