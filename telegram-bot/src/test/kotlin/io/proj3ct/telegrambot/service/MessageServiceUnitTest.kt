package io.proj3ct.telegrambot.service

import io.proj3ct.telegrambot.clients.animeclient.AnimeControllerClient
import io.proj3ct.telegrambot.mapper.toFullStringInfo
import io.proj3ct.telegrambot.utils.AnimeMother
import io.proj3ct.telegrambot.utils.BotAnswers
import io.proj3ct.telegrambot.utils.TestData
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class MessageServiceUnitTest {

    @Mock private lateinit var animeClient: AnimeControllerClient
    @InjectMocks private lateinit var svc: MessageService

    private fun makeMessage(text: String): Message {
        val message = Message().apply { this.text = text }
        message.chat = Chat().apply { id = TestData.CHAT_ID }
        return message
    }

    private fun makeCallback(data: String): CallbackQuery {
        val message = Message().apply { messageId = TestData.MESSAGE_ID }
        message.chat = Chat().apply { id = TestData.CHAT_ID }
        return CallbackQuery().apply {
            this.data = data
            this.message = message
        }
    }

    @Test
    fun `should return greeting when processing START command`() {
        val response = svc.processMessage(makeMessage(TelegramBot.Commands.START.command))
        assertEquals(TestData.CHAT_ID.toString(), response.chatId)
        assertEquals(BotAnswers.START_MESSAGE, response.text)
        assertTrue(response.replyMarkup is InlineKeyboardMarkup)
    }

    @Test
    fun `should ask for anime title when processing DETAILS command`() {
        val botAnswer = svc.processMessage(makeMessage(TelegramBot.Commands.DETAILS.command))
        assertEquals(BotAnswers.ASK_ANIME_TITLE, botAnswer.text)
    }

    @Test
    fun `should show found anime buttons after DETAILS reply`() {
        svc.processMessage(makeMessage(TelegramBot.Commands.DETAILS.command))
        val anime = AnimeMother.getAnimeNameDto()
        whenever(animeClient.searchByTitle(TestData.TITLE))
            .thenReturn(listOf(anime))

        val reply = svc.processMessage(makeMessage(TestData.TITLE))
        assertEquals("Выберите аниме", reply.text)
        val buttons = (reply.replyMarkup as InlineKeyboardMarkup)
            .keyboard.flatten()
        assertEquals(1, buttons.size)
        assertEquals(TestData.TITLE, buttons.single().text)
        assertTrue(
            buttons.single().callbackData!!
                .startsWith(MessageService.State.WAITING_FOR_DETAILS.callbackData!! + anime.id)
        )
    }

    @Test
    fun `should return unknown command when text is unrecognized`() {
        val botAnswer = svc.processMessage(makeMessage("＼(￣▽￣)／"))
        assertEquals(BotAnswers.UNKNOWN_COMMAND, botAnswer.text)
    }

    @Test
    fun `should return full details when handling DETAILS callback`() {
        val anime = AnimeMother.getAnimeDto()
        whenever(animeClient.getDetailsById(anime.id)).thenReturn(anime)

        val callback = makeCallback(MessageService.State.WAITING_FOR_DETAILS.callbackData!! + anime.id)
        val edit = svc.processCallbackQuery(callback)

        assertEquals(TestData.CHAT_ID.toString(), edit.chatId)
        assertEquals(TestData.MESSAGE_ID, edit.messageId)
        assertEquals(anime.toFullStringInfo(), edit.text)
        assertTrue(edit.replyMarkup!!.keyboard.isEmpty())
    }

    @Test
    fun `should show subscribed message when subscribe callback succeeds`() {
        whenever(animeClient.subscribe(TestData.CHAT_ID, 1L)).thenReturn(true)
        val edit = svc.processCallbackQuery(
            makeCallback(MessageService.State.WAITING_FOR_SUBSCRIBE.callbackData!! + 1)
        )
        assertEquals(BotAnswers.SUBSCRIBED, edit.text)
    }

    @Test
    fun `should show subscribe fail when subscribe callback fails`() {
        whenever(animeClient.subscribe(TestData.CHAT_ID, 1L)).thenReturn(false)
        val edit = svc.processCallbackQuery(
            makeCallback(MessageService.State.WAITING_FOR_SUBSCRIBE.callbackData!! + 1)
        )
        assertEquals(BotAnswers.SUBSCRIBE_FAIL, edit.text)
    }

    @Test
    fun `should show unsubscribed message when unsubscribe callback succeeds`() {
        whenever(animeClient.unsubscribe(TestData.CHAT_ID, 1L)).thenReturn(true)
        val edit = svc.processCallbackQuery(
            makeCallback(MessageService.State.WAITING_FOR_UNSUBSCRIBE.callbackData!! + 1)
        )
        assertEquals(BotAnswers.UNSUBSCRIBED, edit.text)
    }

    @Test
    fun `should show unsubscribe fail when unsubscribe callback fails`() {
        whenever(animeClient.unsubscribe(TestData.CHAT_ID, 1L)).thenReturn(false)
        val edit = svc.processCallbackQuery(
            makeCallback(MessageService.State.WAITING_FOR_UNSUBSCRIBE.callbackData!! + 1)
        )
        assertEquals(BotAnswers.UNSUBSCRIBE_FAIL, edit.text)
    }
}
