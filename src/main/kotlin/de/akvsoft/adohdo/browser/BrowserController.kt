package de.akvsoft.adohdo.browser

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

private const val REDIRECT_URL = "redirect:/browser/index.html"
private const val FORWARD_URL = "forward:/browser/index.html"

@Controller
@RequestMapping("/browser")
class BrowserController {

    @GetMapping
    fun root(): String {
        return REDIRECT_URL
    }

    @GetMapping("/login")
    fun login(): String {
        return FORWARD_URL
    }

    @GetMapping("/oauth2/{provider}/redirect")
    fun oauth2(@PathVariable provider: String): String {
        return FORWARD_URL
    }

    @GetMapping("/dashboard")
    fun dashboard(): String {
        return FORWARD_URL
    }

    @GetMapping("/tasks")
    fun tasks(): String {
        return FORWARD_URL
    }
}