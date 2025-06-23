package io.proj3ct.telegrambot.service

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class, SpringExtension::class)
class TelegramBotTest {

    @Spy
    private lateinit var bot: TelegramBot

    @Captor
    private lateinit var captor: ArgumentCaptor<SendMessage>

    @Test
    fun `should send full notification when animeTitles is not empty`() {
        doReturn(Message())
            .`when`(bot)
            .execute(any<SendMessage>())

        val dto = UsersAnimeWithNewEpisodesDto(
            userId = 1L,
            animeTitles = listOf("Naruto", "Bleach")
        )

        bot.notifyUsersAboutNewEpisodes(listOf(dto))

        verify(bot, times(1)).execute(captor.capture())
        val sent = captor.value

        assertEquals("1", sent.chatId)
        assertTrue(sent.text.startsWith("Новые серии для следующих аниме:"))
        assertTrue(sent.text.contains("Naruto"))
        assertTrue(sent.text.contains("Bleach"))
    }
}
