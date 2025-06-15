package org.example.animeservice.models.json

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class AnimeJson(
    val id: Long,
    val name: String,
    val russian: String,
    val url: String,
    val kind: String,
    val score: BigDecimal,
    val status: String,
    val episodes: Int,
    val episodesAired: Int,
    val airedOn: LocalDate,
    val releasedOn: LocalDate,
    val rating: String,
    val duration: Int,
    val description: String?,
    val nextEpisodeAt: LocalDateTime,
    val genres: List<Genre>,
    val studios: List<Studio>
) {

    data class Studio(
        val name: String
    )

    data class Genre(
        val russian: String
    )
}
