// src/test/kotlin/org/example/animeservice/service/UserPreferencesServiceTest.kt
package org.example.animeservice.services

import org.example.animeservice.converters.UserPreferencesConverter
import org.example.animeservice.entities.UserPreferencesEntity
import org.example.animeservice.models.dto.UserPreferencesDto
import org.example.animeservice.repositories.UserPreferencesRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Optional
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UserPreferencesServiceUnitTest {

    @Mock private lateinit var repo: UserPreferencesRepository
    @Mock private lateinit var converter: UserPreferencesConverter
    @InjectMocks private lateinit var svc: UserPreferencesService

    @Test
    fun `when updateGenre then create new entity and save`() {
        whenever(repo.findById(1L)).thenReturn(Optional.empty())
        svc.updateGenre(1L, "Action")

        val captor = argumentCaptor<UserPreferencesEntity>()
        verify(repo).save(captor.capture())
        assertEquals(1L, captor.firstValue.userId)
        assertEquals("Action", captor.firstValue.genre)
    }

    @Test
    fun `when updateStatus then modify existing and save`() {
        val entity = UserPreferencesEntity(2L).apply { status = "Old" }
        whenever(repo.findById(2L)).thenReturn(Optional.of(entity))

        svc.updateStatus(2L, "New")
        assertEquals("New", entity.status)
        verify(repo).save(entity)
    }

    @Test
    fun `when getUserPreferences and exists then return dto and delete`() {
        val entity = UserPreferencesEntity(3L).apply {
            genre = "G"; kind = "K"; status = "S"
        }
        whenever(repo.findById(3L)).thenReturn(Optional.of(entity))
        val dto = UserPreferencesDto(3L, "G", "K", "S")
        whenever(converter.toDto(entity)).thenReturn(dto)

        val result = svc.getUserPreferences(3L)
        assertEquals(dto, result)
        verify(repo).deleteById(3L)
    }

    @Test
    fun `when getUserPreferences and missing then return default and delete`() {
        whenever(repo.findById(4L)).thenReturn(Optional.empty())

        val result = svc.getUserPreferences(4L)
        assertEquals(UserPreferencesDto(4L), result)
        verify(repo).deleteById(4L)
    }
}
