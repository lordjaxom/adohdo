package de.akvsoft.adohdo.event

import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.*

@Service
class AsyncEventService(
    @Lazy private val eventService: EventService
) {

    @Async
    fun send(event: Any, key: UUID, emitter: SseEmitter) {
        try {
            SseEmitter.event()
                .name(event::class.simpleName!!)
                .data(event)
                .let { emitter.send(it) }
        } catch (ex: Exception) {
            eventService.unsubcribe(key, emitter)
            emitter.completeWithError(ex)
        }
    }
}