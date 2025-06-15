package org.example.animeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableAsync
class AnimeServiceApplication

fun main(args: Array<String>) {
    runApplication<AnimeServiceApplication>(*args)
}
