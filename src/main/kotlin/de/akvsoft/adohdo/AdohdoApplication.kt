package de.akvsoft.adohdo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
class AdohdoApplication

fun main(args: Array<String>) {
	runApplication<AdohdoApplication>(*args)
}
