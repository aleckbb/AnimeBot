package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeDto
import org.example.animeservice.providers.AnimeProvider
import org.example.animeservice.repositories.AnimeUserIds
import org.springframework.stereotype.Service

@Service
class AnimeServiceImpl(
    val animeProvider: AnimeProvider
) : AnimeService {

    override fun getDetailsById(id: Long): AnimeDto? = null

    override fun subscribe(chatId: Long, animeId: Long): Boolean = false

    override fun unsubscribe(chatId: Long, animeId: Long): Boolean = false

    override fun updateKind(chatId: Long, kind: String) {
        // пока ничего
    }

    override fun updateGenre(chatId: Long, genre: String) {
        // пока ничего
    }

    override fun updateStatus(chatId: Long, status: String) {
        // пока ничего
    }

    override fun findAllKinds(): List<String> = emptyList()

    override fun findAllGenres(): List<String> = emptyList()

    override fun findAllStatuses(): List<String> = emptyList()

    override fun searchByTitle(query: String): List<AnimeDto> = emptyList()

    override fun searchBySubscribed(chatId: Long): List<AnimeDto> = emptyList()

    override fun getRecommendations(chatId: Long, additionalText: String): List<AnimeDto> = emptyList()

    fun findAnimeWithNewEpisodes(): List<AnimeUserIds> {
        return animeProvider.findAnimeAndSubsWithNewEpisodes()
    }
}
