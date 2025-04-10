package de.akvsoft.adohdo.security.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class OAuth2AuthorizationRequestSerializer {

    private val objectMapper = ObjectMapper()

    fun serialize(value: OAuth2AuthorizationRequest): String =
        Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(value))

    fun deserialize(value: String): OAuth2AuthorizationRequest =
        objectMapper.readValue(value, OAuth2AuthorizationRequest::class.java)
}