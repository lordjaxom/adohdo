package de.akvsoft.adohdo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class AdohdoApplication

fun main(args: Array<String>) {
	runApplication<AdohdoApplication>(*args)
}
