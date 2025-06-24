package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeNameDto
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.example.animeservice.entities.SubscriptionEntity
import org.example.animeservice.repositories.SubscriptionRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class SubscriptionServiceUnitTest {

    @Mock
    private lateinit var repo: SubscriptionRepository

    @Mock
    private lateinit var animeService: AnimeService

    @Mock
    private lateinit var infoComponent: AnimeInfoComponent

    @InjectMocks
    private lateinit var svc: SubscriptionService

    @Test
    fun `when subscribe and already exists then return false`() {
        runBlocking {
            whenever(repo.existsByUserIdAndAnimeId(1, 42)).thenReturn(true)

            val result = svc.subscribe(1, 42)
            assertFalse(result)

            verify(repo, never()).save(any())
            verify(infoComponent, never()).updateAnimeInfo(any())
        }
    }

    @Test
    fun `when subscribe new and anime missing then fetch info and save`() {
        runBlocking {
            whenever(repo.existsByUserIdAndAnimeId(1, 99)).thenReturn(false)
            whenever(animeService.existsAnime(99)).thenReturn(false)

            val result = svc.subscribe(1, 99)
            assertTrue(result)

            verify(infoComponent).updateAnimeInfo(99)

            val captor = argumentCaptor<SubscriptionEntity>()
            verify(repo).save(captor.capture())

            assertThat(captor.firstValue)
                .usingRecursiveComparison()
                .isEqualTo(SubscriptionEntity(userId = 1, animeId = 99))
        }
    }

    @Test
    fun `when unsubscribe existing then return true`() {
        val entity = SubscriptionEntity(1, 42)
        whenever(repo.findByUserIdAndAnimeId(1, 42)).thenReturn(entity)

        val result = svc.unsubscribe(1, 42)
        assertTrue(result)

        verify(repo).delete(entity)
    }

    @Test
    fun `when unsubscribe missing then return false`() {
        whenever(repo.findByUserIdAndAnimeId(1, 42)).thenReturn(null)

        val result = svc.unsubscribe(1, 42)
        assertFalse(result)

        verify(repo, never()).delete(any())
    }

    @Test
    fun `when searchBySubscribed then delegate to animeService`() {
        val subs = listOf(SubscriptionEntity(1, 123), SubscriptionEntity(1, 456))
        whenever(repo.findAllByUserId(1)).thenReturn(subs)
        whenever(animeService.findTitleById(123)).thenReturn("A")
        whenever(animeService.findTitleById(456)).thenReturn("B")

        val result = svc.searchBySubscribed(1)
        assertThat(result).isEqualTo(
            listOf(AnimeNameDto(123, "A"), AnimeNameDto(456, "B"))
        )
    }
}
