package io.proj3ct.telegrambot.service

import io.proj3ct.telegrambot.clients.animeclient.AnimeControllerClient
import io.proj3ct.telegrambot.mapper.toFullStringInfo
import io.proj3ct.telegrambot.utils.AnimeMother
import io.proj3ct.telegrambot.utils.BotAnswers
import io.proj3ct.telegrambot.utils.CommonData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
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

    private lateinit var animeClient: AnimeControllerClient
    private lateinit var svc: MessageService

    @BeforeEach
    fun setup() {
        animeClient = Mockito.mock(AnimeControllerClient::class.java)
        svc = MessageService(animeClient)
    }

    private fun makeMessage(text: String): Message {
        val message = Message().apply { this.text = text }
        message.chat = Chat().apply { id = CommonData.CHAT_ID }
        return message
    }

    private fun makeCallback(data: String): CallbackQuery {
        val message = Message().apply { messageId = CommonData.MESSAGE_ID }
        message.chat = Chat().apply { id = CommonData.CHAT_ID }
        return CallbackQuery().apply {
            this.data = data
            this.message = message
        }
    }

    @Test
    fun `processMessage - START возвращает приветствие`() {
        val response = svc.processMessage(makeMessage(TelegramBot.Commands.START.command))
        assertEquals(CommonData.CHAT_ID.toString(), response.chatId)
        assertEquals(BotAnswers.START_MESSAGE, response.text)
        assertTrue(response.replyMarkup is InlineKeyboardMarkup)
    }

    @Test
    fun `processMessage - DETAILS спрашивает название аниме`() {
        val botAnswer = svc.processMessage(makeMessage(TelegramBot.Commands.DETAILS.command))
        assertEquals(BotAnswers.ASK_ANIME_TITLE, botAnswer.text)
    }

    @Test
    fun `processMessage - после DETAILS выдает кнопки найденных аниме`() {
        svc.processMessage(makeMessage(TelegramBot.Commands.DETAILS.command))
        val anime = AnimeMother.getAnimeNameDto()
        whenever(animeClient.searchByTitle(CommonData.TITLE))
            .thenReturn(listOf(anime))

        val reply = svc.processMessage(makeMessage(CommonData.TITLE))
        assertEquals(CommonData.CHAT_ID.toString(), reply.chatId)
        assertEquals("Выберите аниме", reply.text)

        val markup = reply.replyMarkup as InlineKeyboardMarkup
        val buttons = markup.keyboard.flatten()
        assertEquals(1, buttons.size)
        assertEquals(CommonData.TITLE, buttons.single().text)
        assertTrue(buttons.single().callbackData!!.startsWith(MessageService.State.WAITING_FOR_DETAILS.callbackData!! + anime.id))
    }

    @Test
    fun `processMessage - неизвестное действие`() {
        val botAnswer = svc.processMessage(makeMessage("＼(￣▽￣)／"))
        assertEquals(BotAnswers.UNKNOWN_COMMAND, botAnswer.text)
    }

    @Test
    fun `processCallbackQuery - DETAILS возвращает подробности`() {
        val anime = AnimeMother.getAnimeDto()
        whenever(animeClient.getDetailsById(anime.id)).thenReturn(anime)

        val callbackQuery = makeCallback(MessageService.State.WAITING_FOR_DETAILS.callbackData!! + anime.id)
        val edit = svc.processCallbackQuery(callbackQuery)

        assertEquals(CommonData.CHAT_ID.toString(), edit.chatId)
        assertEquals(CommonData.MESSAGE_ID, edit.messageId)
        assertEquals(anime.toFullStringInfo(), edit.text)
        assertTrue(edit.replyMarkup!!.keyboard.isEmpty())
    }

    @Test
    fun `processCallbackQuery - SUBSCRIBE_SUCCESS`() {
        whenever(animeClient.subscribe(CommonData.CHAT_ID, 1L)).thenReturn(true)
        val edit = svc.processCallbackQuery(makeCallback(MessageService.State.WAITING_FOR_SUBSCRIBE.callbackData!! + 1))
        assertEquals(BotAnswers.SUBSCRIBED, edit.text)
    }

    @Test
    fun `processCallbackQuery - SUBSCRIBE_FAIL`() {
        whenever(animeClient.subscribe(CommonData.CHAT_ID, 1L)).thenReturn(false)
        val edit = svc.processCallbackQuery(makeCallback(MessageService.State.WAITING_FOR_SUBSCRIBE.callbackData!! + 1))
        assertEquals(BotAnswers.SUBSCRIBE_FAIL, edit.text)
    }

    @Test
    fun `processCallbackQuery - UNSUBSCRIBE_SUCCESS`() {
        whenever(animeClient.unsubscribe(CommonData.CHAT_ID, 1L)).thenReturn(true)
        val edit =
            svc.processCallbackQuery(makeCallback(MessageService.State.WAITING_FOR_UNSUBSCRIBE.callbackData!! + 1))
        assertEquals(BotAnswers.UNSUBSCRIBED, edit.text)
    }

    @Test
    fun `processCallbackQuery - UNSUBSCRIBE_FAIL`() {
        whenever(animeClient.unsubscribe(CommonData.CHAT_ID, 1L)).thenReturn(false)
        val edit =
            svc.processCallbackQuery(makeCallback(MessageService.State.WAITING_FOR_UNSUBSCRIBE.callbackData!! + 1))
        assertEquals(BotAnswers.UNSUBSCRIBE_FAIL, edit.text)
    }
}
