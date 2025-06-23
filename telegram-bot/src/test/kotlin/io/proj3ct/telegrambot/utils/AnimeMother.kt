package io.proj3ct.telegrambot.utils

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.anime.dto.AnimeNameDto
import java.math.BigDecimal

object AnimeMother {
    fun getAnimeDto(): AnimeDto {
        return AnimeDto(
            id = 1L,
            name = "Fullmetal Alchemist: Brotherhood",
            russian = "Стальной алхимик: Братство",
            url = "https://myanimelist.net/anime/5114",
            kind = "TV",
            score = BigDecimal("9.22"),
            status = "Finished",
            episodes = 64,
            episodesAired = 64,
            airedOn = TestData.LOCAL_DATE,
            releasedOn = TestData.LOCAL_DATE,
            rating = "PG-13",
            duration = 24,
            description = "Братья Элрик нарушили священный закон и заплатили за это самым дорогим...",
            nextEpisodeAt = TestData.LOCAL_DATE_TIME,
            genres = listOf("Action", "Adventure", "Drama", "Fantasy"),
            studios = listOf("Bones")
        )
    }

    fun getAnimeNameDto(): AnimeNameDto {
        return AnimeNameDto(
            id = 1L,
            name = "Стальной алхимик: Братство",
        )
    }


}