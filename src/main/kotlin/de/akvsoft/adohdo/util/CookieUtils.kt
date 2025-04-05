package de.akvsoft.adohdo.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.Base64

object CookieUtils {

    fun findCookie(request: HttpServletRequest, name: String): Cookie? {
        return request.cookies?.find { it.name == name }
    }

    fun saveCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        response.addCookie(
            Cookie(name, value).apply {
                path = "/"
                isHttpOnly = true
                this.maxAge = maxAge
            }
        )
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        request.cookies
            ?.filter { it.name == name }
            ?.forEach {
                it.apply {
                    value = ""
                    path = "/"
                    maxAge = 0
                    response.addCookie(this)
                }
            }
    }

    fun serialize(value: Any): String {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(value))
    }

    inline fun <reified T> deserialize(value: String): T {
        return SerializationUtils.deserialize(
            Base64.getUrlDecoder().decode(value)
        ) as T
    }
}