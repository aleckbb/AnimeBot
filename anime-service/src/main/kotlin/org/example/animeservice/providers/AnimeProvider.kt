package org.example.animeservice.providers

import io.proj3ct.anime.dto.AnimeDto
import jakarta.transaction.Transactional
import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.repositories.AnimeRepository
import org.example.animeservice.repositories.projections.AnimeUserIds
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

    @Transactional
    fun findTitleById(id: Long): String {
        return animeRepository.findTitleById(id)
    }

    @Transactional
    fun findAllStatuses(): List<String> {
        return animeRepository.findDistinctStatuses()
    }

    @Transactional
    fun findAllKinds(): List<String> {
        return animeRepository.findDistinctKinds()
    }

    @Transactional
    fun findAllGenres(): List<String> {
        return animeRepository.findDistinctGenres()
    }

    @Transactional
    fun existsAnime(id: Long): Boolean {
        return animeRepository.existsById(id)
    }

    @Transactional
    fun getDetailsById(id: Long): AnimeDto {
        return animeRepository.findById(id).map { animeConverter.toDto(it) }.get()
    }
}