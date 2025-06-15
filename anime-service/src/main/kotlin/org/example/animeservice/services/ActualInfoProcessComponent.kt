package org.example.animeservice.services

import kotlinx.coroutines.runBlocking
import org.example.animeservice.clients.shikimoriapiclient.ShikimoriWebClient
import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.providers.AnimeProvider
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ActualInfoProcessComponent(
    private val client: ShikimoriWebClient,
    private val animeProvider: AnimeProvider,
    private val animeConverter: AnimeConverter
) {

    @Async
    fun updateAnimeInfo(animeId: Long) = runBlocking {
        val anime = client.getAnimeInfo(animeId)?.let { animeConverter.toDto(it) }
        if (anime != null) {
            animeProvider.save(anime)
        }
    }
}