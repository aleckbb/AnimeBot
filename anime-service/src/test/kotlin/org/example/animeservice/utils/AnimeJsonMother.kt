package org.example.animeservice.utils

import org.example.animeservice.models.json.AnimeCompactJson
import org.example.animeservice.models.json.AnimeJson
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object AnimeJsonMother {
    fun getAnimeJson(
        id: Long = 1L,
        name: String = "OriginalName",
        russian: String = "РусскоеИмя",
        url: String = "https://example.com/anime/100",
        kind: String = "TV",
        score: BigDecimal = BigDecimal("8.5"),
        status: String = "Finished",
        episodes: Int = 12,
        episodesAired: Int = 12,
        airedOn: LocalDate = LOCAL_DATE,
        releasedOn: LocalDate = LOCAL_DATE,
        rating: String? = "PG-13",
        duration: Int? = 24,
        description: String? = "Test description",
        nextEpisodeAt: LocalDateTime? = null,
        genres: List<AnimeJson.Genre> = emptyList(),
        studios: List<AnimeJson.Studio> = emptyList()
    ): AnimeJson = AnimeJson(
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


    fun getAnimeCompactJson(
        id: Long = 1L,
        name: String = "OriginalName",
        russian: String = "РусскоеИмя",
        url: String = "urlA",
        kind: String = "TV",
        score: BigDecimal = BigDecimal("7.0"),
        status: String = "Finished",
        episodes: Int = 12,
        episodesAired: Int = 12,
        airedOn: LocalDate = LOCAL_DATE,
        releasedOn: LocalDate = LOCAL_DATE,
    ) = AnimeCompactJson(
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
    )

    private val LOCAL_DATE = LocalDate.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
}