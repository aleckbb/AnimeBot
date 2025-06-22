package io.proj3ct.telegrambot.service

import io.proj3ct.anime.dto.AnimeNameDto
import io.proj3ct.telegrambot.clients.animeclient.AnimeControllerClient
import io.proj3ct.telegrambot.mapper.toAnimeNameDto
import io.proj3ct.telegrambot.service.TelegramBot.Commands
import io.proj3ct.telegrambot.utils.BotAnswers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.util.concurrent.ConcurrentHashMap

@Service
class MessageService(private val animeService: AnimeControllerClient) {

    private val logger = LoggerFactory.getLogger(MessageService::class.java)
    private val userStates = ConcurrentHashMap<Long, State>()

    private enum class State(val callbackData: String? = null) {
        IDLE,
        WAITING_FOR_DETAILS("DETAILS"),
        WAITING_FOR_SUBSCRIBE("SUBSCRIBE"),
        WAITING_FOR_UNSUBSCRIBE("UNSUBSCRIBE"),
        WAITING_FOR_KIND("KIND"),
        WAITING_FOR_GENRE("GENRE"),
        WAITING_FOR_STATUS("STATUS"),
        WAITING_FOR_ADDITIONS("ADDITIONS"),
    }

    /* -------------------------------- callback part -------------------------------- */
    fun processCallbackQuery(callbackQuery: CallbackQuery): EditMessageText {
        val data = callbackQuery.data
        val chatId = callbackQuery.message.chatId
        logger.info("[CB] Got callBackQuery: chat=$chatId data=$data")
        logger.info("processCallbackQuery...")

        return when {
            data.startsWith(State.WAITING_FOR_DETAILS.callbackData!!) -> {
                val animeId = data.removePrefix(State.WAITING_FOR_DETAILS.callbackData).toLong()
                val details = animeService.getDetailsById(animeId).also {
                    logger.info("getDetailsById($animeId) -> $it")
                } ?: "Не удалось найти подробности"
                createEditMessageText(callbackQuery, details.toString())
            }

            data.startsWith(State.WAITING_FOR_SUBSCRIBE.callbackData!!) -> {
                val animeId = data.removePrefix(State.WAITING_FOR_SUBSCRIBE.callbackData).toLong()
                val ok = animeService.subscribe(chatId, animeId).also {
                    logger.info("subscribe(chat=$chatId, anime=$animeId) -> $it")
                }
                createEditMessageText(callbackQuery, if (ok) BotAnswers.SUBSCRIBED else BotAnswers.SUBSCRIBE_FAIL)
            }

            data.startsWith(State.WAITING_FOR_UNSUBSCRIBE.callbackData!!) -> {
                val animeId = data.removePrefix(State.WAITING_FOR_UNSUBSCRIBE.callbackData).toLong()
                val ok = animeService.unsubscribe(chatId, animeId).also {
                    logger.info("unsubscribe(chat=$chatId, anime=$animeId) -> $it")
                }
                createEditMessageText(callbackQuery, if (ok) BotAnswers.UNSUBSCRIBED else BotAnswers.UNSUBSCRIBE_FAIL)
            }

            data.startsWith(State.WAITING_FOR_KIND.callbackData!!) -> {
                val kind = data.removePrefix(State.WAITING_FOR_KIND.callbackData)
                animeService.updateKind(chatId, kind).also { logger.info("updateKind(chat=$chatId, kind=$kind)") }
                val genres = animeService.findAllGenres().also { logger.info("findAllGenres() -> $it") }
                val buttons = stringsToButtons(genres, State.WAITING_FOR_GENRE.callbackData)
                createEditMessageText(callbackQuery, "Выберите желаемый жанр", InlineKeyboardMarkup(buttons))
            }

            data.startsWith(State.WAITING_FOR_GENRE.callbackData!!) -> {
                val genre = data.removePrefix(State.WAITING_FOR_GENRE.callbackData)
                animeService.updateGenre(chatId, genre).also { logger.info("updateGenre(chat=$chatId, genre=$genre)") }
                val statuses = animeService.findAllStatuses().also { logger.info("findAllStatuses() -> $it") }
                val buttons = stringsToButtons(statuses, State.WAITING_FOR_STATUS.callbackData)
                createEditMessageText(callbackQuery, "Выберите желаемый статус", InlineKeyboardMarkup(buttons))
            }

            data.startsWith(State.WAITING_FOR_STATUS.callbackData!!) -> {
                val status = data.removePrefix(State.WAITING_FOR_STATUS.callbackData)
                animeService.updateStatus(chatId, status)
                    .also { logger.info("updateStatus(chat=$chatId, status=$status)") }
                val buttons = stringsToButtons(listOf("Да", "Нет"), State.WAITING_FOR_ADDITIONS.callbackData)
                createEditMessageText(callbackQuery, "Что-нибудь еще?", InlineKeyboardMarkup(buttons))
            }

            data.startsWith(State.WAITING_FOR_ADDITIONS.callbackData!!) -> {
                val answer = data.removePrefix(State.WAITING_FOR_ADDITIONS.callbackData)
                if (answer == "Нет") {
                    val recs = animeService.getRecommendations(chatId)
                        .also { logger.info("getRecommendations(chat=$chatId) -> ${it.size} items") }
                    val buttons =
                        animeToButtons(recs.map { it.toAnimeNameDto() }, State.WAITING_FOR_DETAILS.callbackData)
                    createEditMessageText(callbackQuery, "Вот список рекомендаций", InlineKeyboardMarkup(buttons))
                } else {
                    userStates[chatId] =
                        State.WAITING_FOR_ADDITIONS.also { logger.info("State -> $it for chat $chatId") }
                    createEditMessageText(callbackQuery, "Напишите что-нибудь еще")
                }
            }

            else -> createEditMessageText(callbackQuery, "Неизвестное действие")
        }
    }

    /* -------------------------------- message part -------------------------------- */
    fun processMessage(message: Message): SendMessage {
        val chatId = message.chatId
        val text = message.text.trim()
        val state = userStates.getOrDefault(chatId, State.IDLE)
        logger.info("[MSG] Got message: chat=$chatId, text=$text")
        logger.info("processMessage: state=$state...")

        return when (state) {
            State.IDLE -> handleCommand(chatId, text)

            State.WAITING_FOR_DETAILS -> {
                userStates[chatId] = State.IDLE.also { logger.info("State -> IDLE for chat $chatId") }
                val matches =
                    animeService.searchByTitle(text).also { logger.info("searchByTitle('$text') -> ${it.size} items") }
                createMessageWithAnimeButtons(matches, chatId, State.WAITING_FOR_DETAILS.callbackData)
            }

            State.WAITING_FOR_SUBSCRIBE -> {
                userStates[chatId] = State.IDLE.also { logger.info("State -> IDLE for chat $chatId") }
                val matches =
                    animeService.searchByTitle(text).also { logger.info("searchByTitle('$text') -> ${it.size} items") }
                createMessageWithAnimeButtons(matches, chatId, State.WAITING_FOR_SUBSCRIBE.callbackData)
            }

            State.WAITING_FOR_ADDITIONS -> {
                userStates[chatId] = State.IDLE.also { logger.info("State -> IDLE for chat $chatId") }
                val recs = animeService.getRecommendations(chatId, text)
                    .also { logger.info("getRecommendations(chat=$chatId, add='$text') -> ${it.size} items") }
                createMessageWithAnimeButtons(
                    recs.map { it.toAnimeNameDto() },
                    chatId,
                    State.WAITING_FOR_DETAILS.callbackData
                )
            }

            else -> handleCommand(chatId, "")
        }
    }

    /* -------------------------------- command handler -------------------------------- */
    private fun handleCommand(chatId: Long, command: String): SendMessage {
        logger.info("handleCommand: chat=$chatId, cmd='$command'")
        return when (command) {
            Commands.START.command -> createMessage(chatId, BotAnswers.START_MESSAGE)

            Commands.DETAILS.command -> {
                userStates[chatId] = State.WAITING_FOR_DETAILS.also { logger.info("State -> $it for chat $chatId") }
                createMessage(chatId, BotAnswers.ASK_ANIME_TITLE)
            }

            Commands.SUBSCRIBE.command -> {
                userStates[chatId] = State.WAITING_FOR_SUBSCRIBE.also { logger.info("State -> $it for chat $chatId") }
                createMessage(chatId, BotAnswers.ASK_SUBSCRIBE_TITLE)
            }

            Commands.UNSUBSCRIBE.command -> {
                userStates[chatId] = State.IDLE.also { logger.info("State -> IDLE for chat $chatId") }
                val matches = animeService.searchBySubscribed(chatId)
                    .also { logger.info("searchBySubscribed(chat=$chatId) -> ${it.size} items") }
                createMessageWithAnimeButtons(matches, chatId, State.WAITING_FOR_UNSUBSCRIBE.callbackData)
            }

            Commands.RECOMMENDATIONS.command -> {
                val kinds = animeService.findAllKinds().also { logger.info("findAllKinds() -> $it") }
                createMessageWithButtons(kinds, chatId, State.WAITING_FOR_KIND.callbackData)
            }

            Commands.COMMANDS.command -> createMessage(chatId, BotAnswers.COMMANDS_INFO_MESSAGE)

            else -> createMessage(chatId, BotAnswers.UNKNOWN_COMMAND)
        }
    }

    private fun createMessageWithAnimeButtons(
        matches: List<AnimeNameDto>,
        chatId: Long,
        callbackData: String?
    ): SendMessage {
        if (matches.isEmpty()) {
            return createMessage(chatId, "Аниме не найдено.")
        }
        val buttons = animeToButtons(matches, callbackData)
        return createMessage(chatId, "Выберите аниме", InlineKeyboardMarkup(buttons))

    }

    private fun createMessageWithButtons(list: List<String>, chatId: Long, callbackData: String?): SendMessage {
        if (list.isEmpty()) {
            return createMessage(chatId, "Не найдено.")
        }
        val buttons = stringsToButtons(list, callbackData)
        return createMessage(chatId, "Выберите нужный вариант", InlineKeyboardMarkup(buttons))

    }

    private fun animeToButtons(animeList: List<AnimeNameDto>, callbackData: String? = "") = animeList.map { anime ->
        listOf(
            InlineKeyboardButton().apply {
                this.text = anime.name
                this.callbackData = callbackData + "${anime.id}"
            }
        )
    }

    private fun stringsToButtons(list: List<String>, callbackData: String? = "") = list.map {
        listOf(
            InlineKeyboardButton().apply {
                this.text = it
                this.callbackData = callbackData + "${it}"
            }
        )
    }

    private fun createMessage(
        chatId: Long,
        text: String,
        replyMarkup: ReplyKeyboard? = InlineKeyboardMarkup(emptyList())
    ): SendMessage {
        return SendMessage().apply {
            this.chatId = chatId.toString()
            this.text = text
            this.replyMarkup = replyMarkup
        }
    }

    private fun createEditMessageText(
        callbackQuery: CallbackQuery,
        text: String,
        replyMarkup: InlineKeyboardMarkup? = InlineKeyboardMarkup(emptyList())
    ) =
        EditMessageText().apply {
            this.chatId = callbackQuery.message.chatId.toString()
            this.messageId = callbackQuery.message.messageId
            this.text = text
            this.replyMarkup = replyMarkup

        }
}
