package org.example.animeservice.converters

import io.proj3ct.anime.dto.AnimeDto
import org.example.animeservice.entities.AnimeEntity
import org.example.animeservice.models.json.AnimeJson
import org.springframework.stereotype.Component

@Component
class AnimeConverter {

    fun toDto(animeEntity: AnimeEntity): AnimeDto {
        TODO()
    }

    fun toDto(animeJson: AnimeJson): AnimeDto {
        TODO()
    }

    fun toEntity(animeDto: AnimeDto): AnimeEntity {
        TODO()
    }
}