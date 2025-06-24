package org.example.animeservice.services

import io.proj3ct.anime.dto.mother.AnimeMother
import kotlinx.coroutines.runBlocking
import org.example.animeservice.clients.shikimoriapiclient.ShikimoriWebClient
import org.example.animeservice.converters.AnimeConverter
import org.example.animeservice.providers.AnimeProvider
import org.example.animeservice.utils.AnimeJsonMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class AnimeInfoComponentTest {

    @Mock
    private lateinit var client: ShikimoriWebClient

    @Mock
    private lateinit var converter: AnimeConverter

    @Mock
    private lateinit var provider: AnimeProvider

    @InjectMocks
    private lateinit var component: AnimeInfoComponent

    @Test
    fun `when updateAnimeInfo then convert JSON to DTO and save`() {
        runBlocking {
            val dto = AnimeMother.getAnimeDto()
            val json = AnimeJsonMother.getAnimeJson()

            whenever(client.getAnimeInfo(123L)).thenReturn(json)
            whenever(converter.toDto(json)).thenReturn(dto)

            val result = component.updateAnimeInfo(123L)

            assertEquals(dto, result)
            verify(provider).save(dto)
        }
    }

    @Test
    fun `when searchAnime then sort by score desc and prefer russian title if present`() = runBlocking {
        val firstAnime = AnimeJsonMother.getAnimeCompactJson(id = 1L, russian = "", score = BigDecimal("5.0"))
        val secondAnime = AnimeJsonMother.getAnimeCompactJson(id = 2L, russian = "РусскоеИмя", score = BigDecimal("9.0"))

        whenever(client.searchAnime("query")).thenReturn(listOf(firstAnime, secondAnime))

        val result = component.searchAnime("query")

        assertEquals(2, result.size)

        assertEquals(2L, result[0].id)
        assertEquals("РусскоеИмя", result[0].name)
        assertEquals(1L, result[1].id)
        assertEquals("OriginalName", result[1].name)
    }
}
