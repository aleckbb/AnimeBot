package org.example.animebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnimeBotApplication

fun main(args: Array<String>) {
    runApplication<AnimeBotApplication>(*args)
}
