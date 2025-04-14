package de.akvsoft.adohdo.event

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.UUID
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

private val logger = KotlinLogging.logger {}

@Service
class EventService(
    private val asyncEventService: AsyncEventService
) {

    private val emitters = mutableMapOf<UUID, MutableSet<SseEmitter>>()
    private val lock = ReentrantReadWriteLock()

    fun subscribe(uuid: UUID): SseEmitter =
        lock.write {
            return SseEmitter().also {
                emitters
                    .computeIfAbsent(uuid) { mutableSetOf() }
                    .add(it)
            }
        }

    fun emit(event: Any) {
        lock.read {
            emitters.entries
                .asSequence()
                .flatMap { it.value.asSequence().map { emitter -> Pair(it.key, emitter) } }
                .forEach { (key, emitter) -> asyncEventService.send(event, key, emitter) }
        }
    }

    internal fun unsubcribe(key: UUID, emitter: SseEmitter) {
        lock.write {
            emitters[key]
                ?.apply { remove(emitter) }
                ?.apply {
                    if (isEmpty()) {
                        emitters.remove(key)
                    }
                }
        }
    }

    @EventListener(ContextClosedEvent::class)
    fun onContextStopped(event: ContextClosedEvent) {
        logger.info { "Closing all active event streams due to application shutdown" }

        lock.write {
            emitters.values
                .asSequence()
                .flatMap { it.asSequence() }
                .forEach { it.complete() }
            emitters.clear()
        }
    }
}