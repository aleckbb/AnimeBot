package org.example.animeservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "anime")
open class AnimeEntity(

    @Id
    @Column(name = "id", nullable = false)
    open val id: Long,

    @Column(name = "name", nullable = false)
    open val name: String,

    @Column(name = "russian", nullable = false)
    open val russian: String,

    @Column(name = "url", nullable = false)
    open val url: String,

    @Column(name = "kind", nullable = false)
    open val kind: String,

    @Column(name = "score", nullable = false)
    open val score: BigDecimal,

    @Column(name = "status", nullable = false)
    open val status: String,

    @Column(name = "episodes", nullable = false)
    open val episodes: Int,

    @Column(name = "episodes_aired", nullable = false)
    open val episodesAired: Int,

    @Column(name = "aired_on", nullable = false)
    open val airedOn: LocalDate
) {

    @Column(name = "released_on")
    open var releasedOn: LocalDate? = null

    @Column(name = "rating")
    open var rating: String? = null

    @Column(name = "duration")
    open var duration: Int? = null

    @Column(name = "description")
    open var description: String? = null

    @Column(name = "next_episode_at")
    open var nextEpisodeAt: LocalDateTime? = null

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "genres", columnDefinition = "text[]")
    open var genres: List<String>? = null

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "studios", columnDefinition = "text[]")
    open var studios: List<String>? = null
}
