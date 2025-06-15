package org.example.animeservice.services

import kotlinx.coroutines.runBlocking
import org.example.animeservice.clients.shikimoriapiclient.ShikimoriApiClient
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ActualInfoProcessComponent(
    private val client: ShikimoriApiClient,
    private val animeProvider: AnimeProvider
) {

    @Async
    fun updateAnimeInfo(animeId: Int) = runBlocking {
        val anime = client.getAnimeInfo(animeId)
        // дополнительная логика (кэш, БД, уведомления и т.п.)
    }
}