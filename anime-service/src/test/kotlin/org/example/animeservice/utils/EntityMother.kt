package org.example.animeservice.utils

import org.example.animeservice.entities.AnimeEntity
import org.example.animeservice.entities.SubscriptionEntity
import org.example.animeservice.entities.UserPreferencesEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

object EntityMother {
    fun getAnimeEntity(
        id: Long = 1L,
        name: String = "SampleName",
        russian: String = "Образец",
        url: String = "https://example.com/anime/1",
        kind: String = "TV",
        score: BigDecimal = BigDecimal("8.5"),
        status: String = "Finished",
        episodes: Int = 24,
        episodesAired: Int = 12,
        airedOn: LocalDate? = LocalDate.of(2020, 1, 1),
        releasedOn: LocalDate? = LocalDate.of(2020, 1, 15),
        rating: String? = "PG-13",
        duration: Int? = 25,
        description: String? = "Описание аниме...",
        nextEpisodeAt: LocalDateTime? = LocalDateTime.of(2020, 2, 1, 20, 0),
        genres: List<String>? = listOf("Action", "Adventure"),
        studios: List<String>? = listOf("StudioX"),
        userIds: Set<Long> = setOf(42L, 43L)
    ): AnimeEntity {
        return AnimeEntity(
            id = id,
            name = name,
            russian = russian,
            url = url,
            kind = kind,
            score = score,
            status = status,
            episodes = episodes,
            episodesAired = episodesAired
        ).apply {
            this.airedOn = airedOn
            this.releasedOn = releasedOn
            this.rating = rating
            this.duration = duration
            this.description = description
            this.nextEpisodeAt = nextEpisodeAt
            this.genres = genres
            this.studios = studios
            this.userIds = userIds.toMutableSet()
        }
    }

    fun getSubscriptionEntity(
        userId: Long = 1L,
        animeId: Long = 1L,
        id: Long? = 100L
    ): SubscriptionEntity {
        return SubscriptionEntity(
            userId = userId,
            animeId = animeId
        ).apply {
            this.id = id
        }
    }

    fun getUserPreferencesEntity(
        userId: Long = 1L,
        genre: String? = "Action",
        kind: String? = "TV",
        status: String? = "Ongoing"
    ): UserPreferencesEntity {
        return UserPreferencesEntity(userId).apply {
            this.genre = genre
            this.kind = kind
            this.status = status
        }
    }
}
