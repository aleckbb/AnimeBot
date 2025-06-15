package org.example.animeservice.providers

import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.models.dto.AnimeDto
import org.example.animeservice.models.json.AnimeJson
import org.example.animeservice.repositories.AnimeRepository
import org.springframework.stereotype.Component

@Component
class AnimeProvider(
    private val animeRepository: AnimeRepository,
    private val animeConverter: AnimeConverter
) {

    fun save(animeDto: AnimeDto) {
        animeRepository.save(animeConverter.toEntity(animeDto))
    }
}