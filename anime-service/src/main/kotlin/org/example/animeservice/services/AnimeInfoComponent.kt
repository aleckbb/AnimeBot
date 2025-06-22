package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeNameDto
import kotlinx.coroutines.runBlocking
import org.example.animeservice.clients.shikimoriapiclient.ShikimoriWebClient
import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.models.json.AnimeCompactJson
import org.example.animeservice.providers.AnimeProvider
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AnimeInfoComponent(
    private val client: ShikimoriWebClient,
    private val animeProvider: AnimeProvider,
    private val animeConverter: AnimeConverter
) {

    @Async
    fun updateAnimeInfo(animeId: Long) = runBlocking {
        client.getAnimeInfo(animeId)!!.let { animeConverter.toDto(it) }.apply {
            animeProvider.save(this)
        }
    }

    @Async
    fun searchAnime(query: String) = runBlocking {
        val animeList = client.searchAnime(query).sortedBy { -it.score }
        animeList.map { AnimeNameDto(it.id, getTitle(it)) }
    }

    private fun getTitle(anime: AnimeCompactJson): String {
        return if(anime.russian.isEmpty()) anime.russian
        else anime.name
    }
}