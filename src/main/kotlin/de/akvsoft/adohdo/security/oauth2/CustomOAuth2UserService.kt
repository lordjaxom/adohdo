package de.akvsoft.adohdo.security.oauth2

import de.akvsoft.adohdo.security.UserPrincipal
import de.akvsoft.adohdo.security.oauth2.user.OAuth2UserInfo
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService : DefaultOAuth2UserService() {

    companion object {
        val logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)!!
    }

    override fun loadUser(request: OAuth2UserRequest): OAuth2User {
        val user = super.loadUser(request)
        return processOAuth2User(request, user)
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val userInfo = OAuth2UserInfo(
            oAuth2UserRequest.clientRegistration.registrationId,
            oAuth2User.attributes
        )

        if (userInfo.email.isEmpty()) {
            logger.error("Email not found from OAuth2 provider")
            throw IllegalStateException("Email not found from OAuth2 provider")
        }

        return UserPrincipal(
            userInfo.id,
            userInfo.email,
            "",
            oAuth2User.attributes,
        )
    }
}