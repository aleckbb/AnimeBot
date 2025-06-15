package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeDto
import org.example.animeservice.providers.AnimeProvider
import org.example.animeservice.repositories.AnimeUserIds
import org.springframework.stereotype.Service

@Service
class AnimeService(
    val animeProvider: AnimeProvider
) {

    fun getDetailsById(id: Long): AnimeDto? = null

    fun subscribe(chatId: Long, animeId: Long): Boolean = false

    fun unsubscribe(chatId: Long, animeId: Long): Boolean = false

    fun updateKind(chatId: Long, kind: String) {
        // пока ничего
    }

    fun updateGenre(chatId: Long, genre: String) {
        // пока ничего
    }

    fun updateStatus(chatId: Long, status: String) {
        // пока ничего
    }

    fun findAllKinds(): List<String> = emptyList()

    fun findAllGenres(): List<String> = emptyList()

    fun findAllStatuses(): List<String> = emptyList()

    fun searchByTitle(query: String): List<AnimeDto> = emptyList()

    fun searchBySubscribed(chatId: Long): List<AnimeDto> = emptyList()

    fun getRecommendations(chatId: Long, additionalText: String): List<AnimeDto> = emptyList()

    fun findAnimeWithNewEpisodes(): List<AnimeUserIds> {
        return animeProvider.findAnimeAndSubsWithNewEpisodes()
    }
}
