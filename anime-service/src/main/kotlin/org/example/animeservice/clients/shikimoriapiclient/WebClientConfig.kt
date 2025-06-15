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
}