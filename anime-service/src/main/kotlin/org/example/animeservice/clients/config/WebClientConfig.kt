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

    @Bean
    fun gptClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("http://localhost:5500")
            .codecs { it.defaultCodecs().maxInMemorySize(2 * 1024 * 1024) }
            .build()
}