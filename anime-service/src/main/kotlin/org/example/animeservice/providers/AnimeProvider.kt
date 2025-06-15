package org.example.animeservice.providers

import io.proj3ct.anime.dto.AnimeDto
import jakarta.transaction.Transactional
import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.repositories.AnimeUserIds
import org.example.animeservice.repositories.AnimeRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AnimeProvider(
    private val animeRepository: AnimeRepository,
    private val animeConverter: AnimeConverter
) {

    @Transactional
    fun save(animeDto: AnimeDto) {
        animeRepository.save(animeConverter.toEntity(animeDto))
    }

    @Transactional
    fun findAnimeAndSubsWithNewEpisodes(): List<AnimeUserIds> {
        return animeRepository.findTitleAndSubsByNextEpisodeAt(LocalDateTime.now())
    }
}