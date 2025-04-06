package de.akvsoft.adohdo.security.oauth2

data class OAuth2UserInfo(
    val id: String,
    val name: String,
    val email: String,
    val imageUrl: String
)

private fun fromGoogle(attributes: Map<String, Any>) =
    with(attributes) {
        OAuth2UserInfo(
            get("sub") as String,
            get("name") as String,
            get("email") as String,
            get("picture") as String
        )
    }

private fun fromGithub(attributes: Map<String, Any>) =
    with(attributes) {
        OAuth2UserInfo(
            "${get("id") as Int}",
            get("name") as String,
            get("email") as String,
            get("avatar_url") as String
        )
    }

fun OAuth2UserInfo(registrationId: String, attributes: Map<String, Any>) =
    when (registrationId.lowercase()) {
        "google" -> fromGoogle(attributes)
        "github" -> fromGithub(attributes)
        else -> throw IllegalArgumentException("Login with $registrationId is not supported.")
    }
