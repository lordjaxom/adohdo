package de.akvsoft.adohdo.security.oauth2.user

data class OAuth2UserInfo(
    val id: String,
    val name: String,
    val email: String,
    val imageUrl: String
)

private fun fromGoogle(attributes: Map<String, Any>): OAuth2UserInfo {
    attributes.apply {
        return OAuth2UserInfo(
            get("sub") as String,
            get("name") as String,
            get("email") as String,
            get("picture") as String
        )
    }
}

private fun fromGithub(attributes: Map<String, Any>): OAuth2UserInfo {
    attributes.apply {
        return OAuth2UserInfo(
            "${get("id") as Int}",
            get("name") as String,
            get("email") as String,
            get("avatar_url") as String
        )
    }
}

fun OAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
    attributes.apply {
        when (registrationId.lowercase()) {
            "google" -> return fromGoogle(attributes)
            "github" -> return fromGithub(attributes)
        }
    }
    throw IllegalArgumentException("Login with $registrationId is not supported.")
}
