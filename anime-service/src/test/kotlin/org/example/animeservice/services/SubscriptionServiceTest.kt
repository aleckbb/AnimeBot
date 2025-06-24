package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeNameDto
import io.proj3ct.anime.dto.mother.AnimeMother
import kotlinx.coroutines.runBlocking
import org.example.animeservice.IntegrationTest
import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.entities.SubscriptionEntity
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.Test

class SubscriptionServiceTest @Autowired constructor(
    private val subscriptionService: SubscriptionService,
    private val animeConverter: AnimeConverter
) : IntegrationTest() {

    @Test
    fun `should subscribe when anime exists`() = runBlocking {
        // ARRANGE:
        val animeDto = AnimeMother.getAnimeDto(id = 100L)
        animeRepository.save(animeConverter.toEntity(animeDto))

        // ACT
        val result = subscriptionService.subscribe(userId = 1L, animeId = 100L)

        // ASSERT
        assertTrue(result)
        assertTrue(subscriptionRepository.existsByUserIdAndAnimeId(1L, 100L))
    }

    @Test
    fun `should unsubscribe existing subscription`() = runBlocking {
        // ARRANGE
        val animeDto = AnimeMother.getAnimeDto(id = 200L)
        animeRepository.save(animeConverter.toEntity(animeDto))
        subscriptionRepository.save(SubscriptionEntity(userId = 2L, animeId = 200L))

        // ACT
        val result = subscriptionService.unsubscribe(userId = 2L, animeId = 200L)

        // ASSERT
        assertTrue(result)
        assertFalse(subscriptionRepository.findByUserIdAndAnimeId(2L, 200L)?.let { true } ?: false)
    }

    @Test
    fun `should return subscribed anime names`() {
        // ARRANGE
        val dto1 = AnimeMother.getAnimeDto(id = 300L, russian = "Аниме300")
        val dto2 = AnimeMother.getAnimeDto(id = 301L, russian = "Аниме301")
        animeRepository.saveAll(
            listOf(
                animeConverter.toEntity(dto1),
                animeConverter.toEntity(dto2)
            )
        )
        subscriptionRepository.saveAll(
            listOf(
                SubscriptionEntity(userId = 5L, animeId = 300L),
                SubscriptionEntity(userId = 5L, animeId = 301L)
            )
        )

        // ACT
        val names: List<AnimeNameDto> = subscriptionService.searchBySubscribed(5L)

        // ASSERT
        assertEquals(
            listOf(
                AnimeNameDto(id = 300L, name = dto1.russian),
                AnimeNameDto(id = 301L, name = dto2.russian)
            ),
            names
        )
    }
}