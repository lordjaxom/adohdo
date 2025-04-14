package de.akvsoft.adohdo.event

import de.akvsoft.adohdo.security.CustomUserPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {

    @GetMapping
    fun events(@AuthenticationPrincipal principal: CustomUserPrincipal): SseEmitter {
        return eventService.subscribe(principal.id)
    }
}