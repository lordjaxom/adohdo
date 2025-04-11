package de.akvsoft.adohdo.register

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)
