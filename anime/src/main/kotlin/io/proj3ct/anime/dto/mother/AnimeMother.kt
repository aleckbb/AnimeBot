package io.proj3ct.anime.dto.mother

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.anime.dto.AnimeNameDto
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object AnimeMother {

    fun getAnimeDto(
        id: Long = 1L,
        name: String = "Fullmetal Alchemist: Brotherhood",
        russian: String = "Стальной алхимик: Братство",
        url: String = "https://myanimelist.net/anime/5114",
        kind: String = "TV",
        score: BigDecimal = BigDecimal("9.22"),
        status: String = "Finished",
        episodes: Int = 64,
        episodesAired: Int = 64,
        airedOn: LocalDate = LOCAL_DATE,
        releasedOn: LocalDate = LOCAL_DATE,
        rating: String? = "PG-13",
        duration: Int? = 24,
        description: String? = "Братья Элрик нарушили священный закон и заплатили за это самым дорогим...",
        nextEpisodeAt: LocalDateTime? = LOCAL_DATE_TIME,
        genres: List<String> = listOf("Action", "Adventure", "Drama", "Fantasy"),
        studios: List<String> = listOf("Bones")
    ): AnimeDto = AnimeDto(
        id = id,
        name = name,
        russian = russian,
        url = url,
        kind = kind,
        score = score,
        status = status,
        episodes = episodes,
        episodesAired = episodesAired,
        airedOn = airedOn,
        releasedOn = releasedOn,
        rating = rating,
        duration = duration,
        description = description,
        nextEpisodeAt = nextEpisodeAt,
        genres = genres,
        studios = studios
    )

    fun getAnimeNameDto(
        id: Long = 1L,
        name: String = "Стальной алхимик: Братство"
    ): AnimeNameDto = AnimeNameDto(
        id = id,
        name = name
    )

    private val LOCAL_DATE_TIME = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
    private val LOCAL_DATE = LocalDate.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
}