package org.example.animeservice.services

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import kotlinx.coroutines.runBlocking
import org.example.animeservice.clients.telegrambotclient.TelegramBotWebClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class TelegramBotComponent @Autowired constructor(
    private val telegramBotWebClient: TelegramBotWebClient
) {
    @Async
    fun notifyUsersAboutNewEpisodes (
        userEpisodes: List<UsersAnimeWithNewEpisodesDto>
    )  = runBlocking {
        telegramBotWebClient.notifyUsersAboutNewEpisodes(userEpisodes)
    }
}