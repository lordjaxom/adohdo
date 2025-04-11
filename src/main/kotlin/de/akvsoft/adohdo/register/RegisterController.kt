package de.akvsoft.adohdo.register

import net.minidev.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/register")
class RegisterController(
    private val registerService: RegisterService,
) {

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun register(@RequestBody request: RegisterRequest) {
        registerService.register(request);
    }

    @ExceptionHandler(UserExistsException::class)
    fun handleUserExistsException(ex: UserExistsException): ResponseEntity<String> {
        return ResponseEntity("\"${ex.message}\"", HttpStatus.CONFLICT)
    }
}