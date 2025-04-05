package de.akvsoft.adohdo.security.oauth2

import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.util.StringUtils

val tokenResponseParameterNames = setOf(
    OAuth2ParameterNames.ACCESS_TOKEN,
    OAuth2ParameterNames.TOKEN_TYPE,
    OAuth2ParameterNames.EXPIRES_IN,
    OAuth2ParameterNames.REFRESH_TOKEN,
    OAuth2ParameterNames.SCOPE
)

class CustomAccessTokenResponseConverter : Converter<Map<String, Any>, OAuth2AccessTokenResponse> {

    companion object {
        val logger = LoggerFactory.getLogger(CustomAccessTokenResponseConverter::class.java)!!
    }

    override fun convert(source: Map<String, Any>): OAuth2AccessTokenResponse? {
        val accessToken = source[OAuth2ParameterNames.ACCESS_TOKEN] as String
        val accessTokenType = TokenType.BEARER
        val expiresIn = source[OAuth2ParameterNames.EXPIRES_IN]?.let { (it as Int).toLong() } ?: 0
        val scopes = source[OAuth2ParameterNames.SCOPE]
            ?.let { StringUtils.delimitedListToStringArray(it as String, " ") }
            ?.toSet() ?: emptySet()
        val additionalParameters = source.filterKeys { !tokenResponseParameterNames.contains(it) }

        return OAuth2AccessTokenResponse
            .withToken(accessToken)
            .tokenType(accessTokenType)
            .expiresIn(expiresIn)
            .scopes(scopes)
            .additionalParameters(additionalParameters)
            .build()
    }
}