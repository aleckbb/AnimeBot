package org.example.animeservice.models.json

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
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

    @JsonProperty(value = "episodes_aired")
    val episodesAired: Int,

    @JsonProperty(value = "aired_on")
    val airedOn: LocalDate?,

    @JsonProperty(value = "released_on")
    val releasedOn: LocalDate?,
    val rating: String?,
    val duration: Int?,
    val description: String?,

    @JsonProperty(value = "next_episode_at")
    @field:JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val nextEpisodeAt: LocalDateTime?,
    val genres: List<Genre>?,
    val studios: List<Studio>?
) {

    data class Studio(
        val name: String
    )

    data class Genre(
        val russian: String
    )
}
