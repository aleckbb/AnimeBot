package org.example.animeservice.models.json

import com.fasterxml.jackson.annotation.JsonProperty
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

    @JsonProperty(value = "episodes_aired")
    val episodesAired: Int,

    @JsonProperty(value = "aired_on")
    val airedOn: LocalDate,

    @JsonProperty(value = "released_on")
    val releasedOn: LocalDate?,
)
