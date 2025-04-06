package de.akvsoft.adohdo.security.oauth2

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

class CustomAuthorizationRequestResolver(
    repository: ClientRegistrationRepository,
    authorizationRequestBaseUri: String
) : OAuth2AuthorizationRequestResolver {

    private val defaultResolver = DefaultOAuth2AuthorizationRequestResolver(repository, authorizationRequestBaseUri)
    private val secureKeyGenerator = Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96)

    override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return defaultResolver.resolve(request)?.let { customizeAuthorizationRequest(it) }
    }

    override fun resolve(request: HttpServletRequest, clientRegistrationId: String): OAuth2AuthorizationRequest? {
        return defaultResolver.resolve(request, clientRegistrationId)?.let { customizeAuthorizationRequest(it) }
    }

    private fun customizeAuthorizationRequest(req: OAuth2AuthorizationRequest): OAuth2AuthorizationRequest? {
        val attributes = req.attributes.toMutableMap()
        val additionalParameters = req.additionalParameters.toMutableMap()
        addPkceParameters(attributes, additionalParameters)
        return OAuth2AuthorizationRequest
            .from(req)
            .attributes(attributes)
            .additionalParameters(additionalParameters)
            .build()
    }

    private fun addPkceParameters(attributes: MutableMap<String, Any>, additionalParameters: MutableMap<String, Any>) {
        attributes[PkceParameterNames.CODE_VERIFIER] = secureKeyGenerator.generateKey()
        additionalParameters[PkceParameterNames.CODE_CHALLENGE] = createHash(secureKeyGenerator.generateKey())
        additionalParameters[PkceParameterNames.CODE_CHALLENGE_METHOD] = "S256"
    }
}

private fun createHash(value: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(value.toByteArray(StandardCharsets.US_ASCII))
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
}