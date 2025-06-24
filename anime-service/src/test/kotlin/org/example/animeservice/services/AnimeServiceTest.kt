package org.example.animeservice.services

import io.proj3ct.anime.dto.mother.AnimeMother
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.example.animeservice.IntegrationTest
import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.entities.SubscriptionEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AnimeServiceTest @Autowired constructor(
    private val animeService: AnimeService,
    private val animeConverter: AnimeConverter
) : IntegrationTest() {

    val animeDto = AnimeMother.getAnimeDto()

    @BeforeEach
    fun setup() {
        val animeEntity = animeConverter.toEntity(animeDto)
        animeRepository.save(animeEntity)
    }

    @Test
    fun `should return anime title by id`() {
        // ACT
        val title = animeService.findTitleById(1L)

        // ASSERT
        Assertions.assertEquals(animeDto.russian, title)
    }

    @Test
    fun `should throw exception when id is invalid`() {
        // ACT & ASSERT
        Assertions.assertThrows(EmptyResultDataAccessException::class.java) {
            animeService.findTitleById(2L)
        }
    }

    @Test
    fun `should return true when anime is exist`() {
        // ACT
        val result = animeService.existsAnime(1L)

        // ASSERT
        Assertions.assertTrue(result)
    }

    @Test
    fun `should return anime details by id`() {
        // ACT
        val animeDetails = runBlocking { animeService.getDetailsById(1L) }

        // ASSERT
        Assertions.assertNotNull(animeDetails)
        Assertions.assertEquals(animeDto, animeDetails)
    }

    @Test
    fun `should return list of kinds`() {
        // ACT
        val kinds = animeService.findAllKinds()

        // ASSERT
        Assertions.assertEquals(listOf(animeDto.kind), kinds)
    }

    @Test
    fun `should return list of genres`() {
        // ACT
        val genres = animeService.findAllGenres()

        // ASSERT
        assertThat(genres)
            .containsExactlyInAnyOrderElementsOf(animeDto.genres)
    }

    @Test
    fun `should return list of statuses`() {
        // ACT
        val statuses = animeService.findAllStatuses()

        // ASSERT
        Assertions.assertEquals(listOf(animeDto.status), statuses)
    }

    @Test
    fun `should return list of animeUserIds`() {
        // ARRANGE
        val anotherAnimeDto = animeDto.copy(
            status = "ongoing",
            id = 2L,
            nextEpisodeAt = LocalDateTime.now().minusDays(1),
            russian = "ДругоеИмя"
        )
        animeRepository.save(animeConverter.toEntity(anotherAnimeDto))
        subscriptionRepository.save(SubscriptionEntity(1L, 1L))
        subscriptionRepository.save(SubscriptionEntity(1L, 2L))

        // ACT
        val animeUserIds = animeService.findAnimeWithNewEpisodes()
        val actual = animeUserIds.first()

        // ASSERT
        assertEquals(setOf(1L), actual.getUserIds())
        assertEquals(anotherAnimeDto.russian, actual.getTitle())
        assertEquals(2L, actual.getId())
    }
}