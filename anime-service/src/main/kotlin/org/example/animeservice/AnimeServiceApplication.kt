package org.example.animeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnimeServiceApplication

fun main(args: Array<String>) {
    runApplication<AnimeServiceApplication>(*args)
}
