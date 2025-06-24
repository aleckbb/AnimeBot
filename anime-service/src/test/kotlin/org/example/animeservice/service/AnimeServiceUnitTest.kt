package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeNameDto
import io.proj3ct.anime.dto.mother.AnimeMother
import kotlinx.coroutines.runBlocking
import org.example.animeservice.providers.AnimeProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class AnimeServiceTest {

    @Mock
    private lateinit var provider: AnimeProvider

    @Mock
    private lateinit var component: AnimeInfoComponent

    @InjectMocks
    private lateinit var service: AnimeService

    @Test
    fun `when findTitleById then delegate to provider`() {
        `when`(provider.findTitleById(10L)).thenReturn("MyTitle")
        val result = service.findTitleById(10L)
        assertEquals("MyTitle", result)
        verify(provider).findTitleById(10L)
    }

    @Test
    fun `when existsAnime then delegate to provider`() {
        `when`(provider.existsAnime(20L)).thenReturn(true)
        val result = service.existsAnime(20L)
        assertEquals(true, result)
        verify(provider).existsAnime(20L)
    }

    @Test
    fun `when getDetailsById and anime exists then return from provider`() = runBlocking {
        val dto = AnimeMother.getAnimeDto(id = 42L)
        `when`(provider.existsAnime(42L)).thenReturn(true)
        `when`(provider.getDetailsById(42L)).thenReturn(dto)

        val result = service.getDetailsById(42L)
        assertEquals(dto, result)
        // провайдер.getDetailsById должен быть вызван, компонент — нет
        verify(provider).getDetailsById(42L)
        verifyNoInteractions(component)
    }

    @Test
    fun `when getDetailsById and anime missing then fetch via component`() {
        runBlocking {
            val dto = AnimeMother.getAnimeDto(id = 99L)
            `when`(provider.existsAnime(99L)).thenReturn(false)
            `when`(component.updateAnimeInfo(99L)).thenReturn(dto)

            val result = service.getDetailsById(99L)
            assertEquals(dto, result)
            // провайдер.getDetailsById не должен вызываться, только existsAnime
            verify(provider).existsAnime(99L)
            verify(component).updateAnimeInfo(99L)
            verify(provider, never()).getDetailsById(anyLong())
        }
    }

    @Test
    fun `when findAllKinds then delegate to provider`() {
        val kinds = listOf("TV", "Movie")
        `when`(provider.findAllKinds()).thenReturn(kinds)

        val result = service.findAllKinds()
        assertEquals(kinds, result)
        verify(provider).findAllKinds()
    }

    @Test
    fun `when findAllGenres then delegate to provider`() {
        val genres = listOf("Action", "Drama")
        `when`(provider.findAllGenres()).thenReturn(genres)

        val result = service.findAllGenres()
        assertEquals(genres, result)
        verify(provider).findAllGenres()
    }

    @Test
    fun `when findAllStatuses then delegate to provider`() {
        val statuses = listOf("Ongoing", "Finished")
        `when`(provider.findAllStatuses()).thenReturn(statuses)

        val result = service.findAllStatuses()
        assertEquals(statuses, result)
        verify(provider).findAllStatuses()
    }

    @Test
    fun `when searchByTitle then delegate to component`() {
        runBlocking {
            val names = listOf(
                AnimeNameDto(id = 1L, name = "A"),
                AnimeNameDto(id = 2L, name = "B")
            )
            `when`(component.searchAnime("foo")).thenReturn(names)

            val result = service.searchByTitle("foo")
            assertEquals(names, result)
            verify(component).searchAnime("foo")
        }
    }

    @Test
    fun `when findAnimeWithNewEpisodes then delegate to provider`() {
        val subs: List<org.example.animeservice.repositories.projections.AnimeUserIds> = emptyList()
        `when`(provider.findAnimeAndSubsWithNewEpisodes()).thenReturn(subs)

        val result = service.findAnimeWithNewEpisodes()
        assertEquals(subs, result)
        verify(provider).findAnimeAndSubsWithNewEpisodes()
    }
}
