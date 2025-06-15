package org.example.animeservice.clients.shikimoriapiclient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun shikimoriWebClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("https://shikimori.one/api/animes")
            .build()

    @Bean
    fun telegramBotClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("http://localhost:8081")
            .build()
}