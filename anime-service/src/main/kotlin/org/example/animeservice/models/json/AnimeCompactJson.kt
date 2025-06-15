package org.example.animeservice.models.json

import java.math.BigDecimal
import java.time.LocalDate

data class AnimeCompactJson(
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
    val releasedOn: LocalDate?,
)
