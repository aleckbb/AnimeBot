package org.example.animeservice.service

import io.proj3ct.anime.dto.mother.AnimeMother
import kotlinx.coroutines.runBlocking
import org.example.animeservice.providers.AnimeProvider
import org.example.animeservice.repositories.projections.AnimeUserIds
import org.example.animeservice.services.AnimeInfoComponent
import org.example.animeservice.services.AnimeService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class AnimeServiceUnitTest {

    @Mock
    private lateinit var provider: AnimeProvider

    @Mock
    private lateinit var component: AnimeInfoComponent

    @InjectMocks
    private lateinit var service: AnimeService

    @Test
    fun `when findTitleById then delegate to provider`() {
        whenever(provider.findTitleById(1L)).thenReturn("Title1")

        val result = service.findTitleById(1L)

        assertEquals("Title1", result)
    }

    @Test
    fun `when existsAnime then delegate to provider`() {
        whenever(provider.existsAnime(2L)).thenReturn(true)

        val result = service.existsAnime(2L)

        assertEquals(true, result)
    }

    @Test
    fun `when getDetailsById and anime exists then return provider result`() = runBlocking {
        val dto = AnimeMother.getAnimeDto(id = 42L)
        whenever(provider.existsAnime(dto.id)).thenReturn(true)
        whenever(provider.getDetailsById(dto.id)).thenReturn(dto)

        val result = service.getDetailsById(dto.id)

        assertEquals(dto, result)
        verifyNoInteractions(component)
    }

    @Test
    fun `when getDetailsById and anime missing then fetch via component`() = runBlocking {
        whenever(provider.existsAnime(99L)).thenReturn(false)
        val expected = AnimeMother.getAnimeDto()
        whenever(component.updateAnimeInfo(99L)).thenReturn(expected)

        val actual = service.getDetailsById(99L)

        assertEquals(expected, actual)
        verify(provider).existsAnime(99L)
        verify(provider, never()).getDetailsById(any())
        verifyNoMoreInteractions(provider)
    }


    @Test
    fun `when findAllKinds then delegate to provider`() {
        val kinds = listOf("TV", "Movie")
        whenever(provider.findAllKinds()).thenReturn(kinds)

        val result = service.findAllKinds()

        assertEquals(kinds, result)
    }

    @Test
    fun `when findAllGenres then delegate to provider`() {
        val genres = listOf("Action", "Drama")
        whenever(provider.findAllGenres()).thenReturn(genres)

        val result = service.findAllGenres()

        assertEquals(genres, result)
    }

    @Test
    fun `when findAllStatuses then delegate to provider`() {
        val statuses = listOf("Ongoing", "Finished")
        whenever(provider.findAllStatuses()).thenReturn(statuses)

        val result = service.findAllStatuses()

        assertEquals(statuses, result)
    }

    @Test
    fun `when searchByTitle then delegate to component`() = runBlocking {
        val names = listOf(
            AnimeMother.getAnimeNameDto(id = 1L, name = "A"),
            AnimeMother.getAnimeNameDto(id = 2L, name = "B")
        )
        whenever(component.searchAnime("foo")).thenReturn(names)

        val result = service.searchByTitle("foo")

        assertEquals(names, result)
    }

    @Test
    fun `when findAnimeWithNewEpisodes then delegate to provider`() {
        val stubProjections = listOf(
            object : AnimeUserIds {
                override fun getUserIds(): Set<Long> = setOf(123L)
                override fun getTitle(): String = "Naruto"
                override fun getId(): Long = 42L
            }
        )

        whenever(provider.findAnimeAndSubsWithNewEpisodes()).thenReturn(stubProjections)

        val result = service.findAnimeWithNewEpisodes()

        assertEquals(stubProjections, result)
    }
}
