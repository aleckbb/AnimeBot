package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.anime.dto.AnimeNameDto
import org.example.animeservice.providers.AnimeProvider
import org.example.animeservice.repositories.projections.AnimeUserIds
import org.springframework.stereotype.Service

@Service
class AnimeService(
    private val animeProvider: AnimeProvider,
    private val animeInfoComponent: AnimeInfoComponent
) {

    fun findTitleById(id: Long): String {
        return animeProvider.findTitleById(id)
    }

    fun existsAnime(id: Long): Boolean {
        return animeProvider.existsAnime(id)
    }

    fun getDetailsById(id: Long): AnimeDto {
        return if (existsAnime(id)) {
            animeProvider.getDetailsById(id)
        } else {
            animeInfoComponent.updateAnimeInfo(id)
        }
    }

    fun findAllKinds(): List<String> {
        return animeProvider.findAllKinds()
    }

    fun findAllGenres(): List<String> {
        return animeProvider.findAllGenres()
    }

    fun findAllStatuses(): List<String> {
        return animeProvider.findAllStatuses()
    }

    fun searchByTitle(query: String): List<AnimeNameDto> {
        return animeInfoComponent.searchAnime(query)
    }

    fun findAnimeWithNewEpisodes(): List<AnimeUserIds> {
        return animeProvider.findAnimeAndSubsWithNewEpisodes()
    }
}
