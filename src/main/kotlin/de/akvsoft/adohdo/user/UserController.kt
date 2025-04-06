package de.akvsoft.adohdo.user

import de.akvsoft.adohdo.security.CustomUserPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController {

    @GetMapping
    fun getCurrentUser(@AuthenticationPrincipal principal: CustomUserPrincipal): UserResponse {
        return UserResponse(principal.name)
    }
}