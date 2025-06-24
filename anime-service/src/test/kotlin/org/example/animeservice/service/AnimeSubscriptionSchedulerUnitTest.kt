package org.example.animeservice.service

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import kotlinx.coroutines.runBlocking
import org.example.animeservice.repositories.projections.AnimeUserIds
import org.example.animeservice.services.AnimeInfoComponent
import org.example.animeservice.services.AnimeService
import org.example.animeservice.services.AnimeSubscriptionScheduler
import org.example.animeservice.services.TelegramBotComponent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class AnimeSubscriptionSchedulerUnitTest {

    @Mock
    private lateinit var infoComponent: AnimeInfoComponent
    @Mock
    private lateinit var telegramBot: TelegramBotComponent
    @Mock
    private lateinit var animeService: AnimeService
    @InjectMocks
    private lateinit var scheduler: AnimeSubscriptionScheduler

    private fun fakeRecord(id: Long, title: String, userIds: Set<Long>): AnimeUserIds =
        object : AnimeUserIds {
            override fun getId() = id
            override fun getTitle() = title
            override fun getUserIds() = userIds
        }

    @Test
    fun `when no new episodes then do not notify`() {
        runBlocking {
            whenever(animeService.findAnimeWithNewEpisodes()).thenReturn(emptyList())
            scheduler.checkNewEpisodes()
            verify(telegramBot, never()).notifyUsersAboutNewEpisodes(any())
            verify(infoComponent, never()).updateAnimeInfo(any())
        }
    }

    @Test
    fun `when new episodes then group by user and notify`() {
        runBlocking {
            val rec1 = fakeRecord(1, "A", setOf(1, 2))
            val rec2 = fakeRecord(2, "B", setOf(2))
            whenever(animeService.findAnimeWithNewEpisodes()).thenReturn(listOf(rec1, rec2))

            scheduler.checkNewEpisodes()

            val expected = listOf(
                UsersAnimeWithNewEpisodesDto(1, listOf("A")),
                UsersAnimeWithNewEpisodesDto(2, listOf("A", "B"))
            )
            verify(telegramBot).notifyUsersAboutNewEpisodes(expected)
            verify(infoComponent).updateAnimeInfo(1)
            verify(infoComponent).updateAnimeInfo(2)
        }
    }
}
