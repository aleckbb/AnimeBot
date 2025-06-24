// src/test/kotlin/org/example/animeservice/service/TelegramBotComponentTest.kt
package org.example.animeservice.services

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import org.example.animeservice.clients.telegrambotclient.TelegramBotWebClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class TelegramBotComponentUnitTest {

    @Mock private lateinit var webClient: TelegramBotWebClient
    @InjectMocks private lateinit var component: TelegramBotComponent

    @Test
    fun `when notifyUsersAboutNewEpisodes then delegate to webClient`() {
        val payload = listOf(UsersAnimeWithNewEpisodesDto(1, listOf("A")))
        component.notifyUsersAboutNewEpisodes(payload)
        verify(webClient).notifyUsersAboutNewEpisodes(payload)
    }
}
