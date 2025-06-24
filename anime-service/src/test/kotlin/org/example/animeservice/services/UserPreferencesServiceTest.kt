package org.example.animeservice.services

import org.example.animeservice.IntegrationTest
import org.example.animeservice.entities.UserPreferencesEntity
import org.example.animeservice.models.dto.UserPreferencesDto
import org.example.animeservice.utils.EntityMother
import org.example.animeservice.utils.PreferencesMother
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.Test

class UserPreferencesServiceTest @Autowired constructor(
    private val userPreferencesService: UserPreferencesService
) : IntegrationTest() {

    @Test
    fun `should create preferences when none exist`() {
        // ACT
        userPreferencesService.updateKind(userId = 10L, kind = "Action")

        // ASSERT
        val saved = userPreferencesRepository.findById(10L).orElseThrow()
        assertEquals("Action", saved.kind)
    }

    @Test
    fun `should update existing genre`() {
        // ARRANGE
        val entity = UserPreferencesEntity(20L).apply { genre = "Old" }
        userPreferencesRepository.save(entity)

        // ACT
        userPreferencesService.updateGenre(userId = 20L, genre = "NewGenre")

        // ASSERT
        val updated = userPreferencesRepository.findById(20L).orElseThrow()
        assertEquals("NewGenre", updated.genre)
    }

    @Test
    fun `should update existing status`() {
        // ARRANGE
        val entity = UserPreferencesEntity(30L).apply { status = "OldStatus" }
        userPreferencesRepository.save(entity)

        // ACT
        userPreferencesService.updateStatus(userId = 30L, status = "NewStatus")

        // ASSERT
        val updated = userPreferencesRepository.findById(30L).orElseThrow()
        assertEquals("NewStatus", updated.status)
    }

    @Test
    fun `should return preferences dto and delete entity when exists`() {
        // ARRANGE
        val savedEntity = userPreferencesRepository.save(
            UserPreferencesEntity(40L).apply {
                genre = "Comedy"
                kind = "TV"
                status = "Ongoing"
            }
        )

        // ACT
        val result: UserPreferencesDto = userPreferencesService.getUserPreferences(userId = 40L)

        // ASSERT
        val expectedDto = PreferencesMother.getUserPreferencesDto(
            chatId = 40L,
            genre = "Comedy",
            kind = "TV",
            status = "Ongoing"
        )
        assertEquals(expectedDto, result)
        assertFalse(userPreferencesRepository.existsById(40L))
    }

    @Test
    fun `should return default dto when none exist`() {
        // ARRANGE
        userPreferencesRepository.save(
            EntityMother.getUserPreferencesEntity(50L)
        )

        // ACT
        val result: UserPreferencesDto = userPreferencesService.getUserPreferences(userId = 50L)

        // ASSERT
        val expectedDto = PreferencesMother.getUserPreferencesDto(chatId = 50L)
        assertEquals(expectedDto, result)
        assertFalse(userPreferencesRepository.existsById(50L))
    }
}