package org.example.animeservice.converters

import io.proj3ct.anime.dto.AnimeDto
import org.example.animeservice.entities.AnimeEntity
import org.example.animeservice.models.json.AnimeJson
import org.springframework.stereotype.Component

@Component
class AnimeConverter {

    fun toDto(animeEntity: AnimeEntity): AnimeDto {
        return AnimeDto(
            id = animeEntity.id,
            name = animeEntity.name,
            russian = animeEntity.russian,
            url = animeEntity.url,
            kind = animeEntity.kind,
            score = animeEntity.score,
            status = animeEntity.status,
            episodes = animeEntity.episodes,
            episodesAired = animeEntity.episodesAired,
            airedOn = animeEntity.airedOn,
            releasedOn = animeEntity.releasedOn,
            rating = animeEntity.rating,
            duration = animeEntity.duration,
            description = animeEntity.description,
            nextEpisodeAt = animeEntity.nextEpisodeAt,
            genres = animeEntity.genres,
            studios = animeEntity.studios
        )
    }

    fun toDto(animeJson: AnimeJson): AnimeDto {
        return AnimeDto(
            id = animeJson.id,
            name = animeJson.name,
            russian = animeJson.russian,
            url = animeJson.url,
            kind = animeJson.kind,
            score = animeJson.score,
            status = animeJson.status,
            episodes = animeJson.episodes,
            episodesAired = animeJson.episodesAired,
            airedOn = animeJson.airedOn,
            releasedOn = animeJson.releasedOn,
            rating = animeJson.rating,
            duration = animeJson.duration,
            description = animeJson.description,
            nextEpisodeAt = animeJson.nextEpisodeAt,
            genres = animeJson.genres?.map{ it.russian },
            studios = animeJson.studios?.map{ it.name }
        )
    }

    fun toEntity(animeDto: AnimeDto): AnimeEntity {
        return AnimeEntity(
            id = animeDto.id,
            name = animeDto.name,
            russian = animeDto.russian,
            url = animeDto.url,
            kind = animeDto.kind,
            score = animeDto.score,
            status = animeDto.status,
            episodes = animeDto.episodes,
            episodesAired = animeDto.episodesAired
        ).apply {
            airedOn = animeDto.airedOn
            releasedOn = animeDto.releasedOn
            rating = animeDto.rating
            duration = animeDto.duration
            description = animeDto.description
            nextEpisodeAt = animeDto.nextEpisodeAt
            genres = animeDto.genres
            studios = animeDto.studios
        }
    }
}