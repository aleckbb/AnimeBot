package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeDto
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

    suspend fun updateAnimeInfo(animeId: Long): AnimeDto =
        client.getAnimeInfo(animeId)!!
            .let(animeConverter::toDto)
            .also(animeProvider::save)

    suspend fun searchAnime(query: String): List<AnimeNameDto> {
        val animeList = client.searchAnime(query).sortedBy { -it.score }
        return animeList.map { AnimeNameDto(it.id, getTitle(it)) }
    }

    private fun getTitle(anime: AnimeCompactJson): String {
        return if(anime.russian.isEmpty()) anime.russian
        else anime.name
    }
}