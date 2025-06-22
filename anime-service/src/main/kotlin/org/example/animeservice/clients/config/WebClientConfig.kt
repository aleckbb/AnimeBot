package org.example.animeservice.clients.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun shikimoriClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("https://shikimori.one/api/animes")
            .build()

    @Bean
    fun telegramBotClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("http://localhost:8081/api/telegram-bot")
            .build()
}