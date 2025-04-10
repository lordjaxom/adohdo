package de.akvsoft.adohdo

import kotlin.random.Random
import kotlin.test.Test

class KeyGen {

    @Test
    fun keyGen() {
        val rng = Random.Default
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        println((0..128).map { chars[rng.nextInt(chars.length)] }.toCharArray().joinToString(""))

    }
}