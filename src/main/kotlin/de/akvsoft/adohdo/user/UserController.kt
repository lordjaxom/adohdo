package de.akvsoft.adohdo.user

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUser(): User {
        return User("Sascha Volkenandt")
    }
}