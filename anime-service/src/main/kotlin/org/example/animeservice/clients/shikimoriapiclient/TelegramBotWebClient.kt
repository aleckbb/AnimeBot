package org.example.animeservice.clients.shikimoriapiclient

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class TelegramBotWebClient(
    private val telegramBotClient: WebClient
) {


}