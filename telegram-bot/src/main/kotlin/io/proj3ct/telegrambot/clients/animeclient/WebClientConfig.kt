package io.proj3ct.telegrambot.clients.animeclient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun animeWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("http://localhost:8080/api/anime").build()
}
