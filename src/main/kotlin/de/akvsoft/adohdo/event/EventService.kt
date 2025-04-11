package de.akvsoft.adohdo.event

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

private val logger = KotlinLogging.logger {}

@Service
class EventService {

    private val emitters = mutableListOf<SseEmitter>()

    fun register(): SseEmitter {
        synchronized(this) {
            return SseEmitter()
                .also { emitters.add(it) }
        }
    }

    @Scheduled(fixedDelay = 1000)
    fun emit() {
        synchronized(this) {
            logger.info { "Sending events to ${emitters.size} emitters." }
            emitters.removeIf {
                try {
                    it.send("Hallo Welt")
                    false
                } catch (e: Exception) {
                    it.completeWithError(e)
                    true
                }
            }
        }
    }
}

