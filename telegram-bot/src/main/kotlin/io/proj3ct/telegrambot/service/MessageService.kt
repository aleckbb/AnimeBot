package io.proj3ct.telegrambot.service

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.telegrambot.clients.animeclient.AnimeControllerClient
import io.proj3ct.telegrambot.service.TelegramBot.Commands
import io.proj3ct.telegrambot.utils.BotAnswers
import org.springframework.beans.factory.annotation.Autowired
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
class MessageService @Autowired constructor(
    private val animeService: AnimeControllerClient,
){

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

    fun processCallbackQuery(callbackQuery: CallbackQuery): EditMessageText {
        val data = callbackQuery.data
        val chatId = callbackQuery.message.chatId
        return when {
            data.startsWith(State.WAITING_FOR_DETAILS.callbackData!!) -> {
                val animeId = data.removePrefix(State.WAITING_FOR_DETAILS.callbackData).toLong()
                val detailsText = animeService.getDetailsById(animeId)
                    ?: "Не удалось найти подробности"

                createEditMessageText(callbackQuery, "")
            }

            data.startsWith(State.WAITING_FOR_SUBSCRIBE.callbackData!!) -> {
                val animeId = data.removePrefix(State.WAITING_FOR_SUBSCRIBE.callbackData).toLong()
                val ok = animeService.subscribe(chatId, animeId)

                createEditMessageText(callbackQuery, if (ok) BotAnswers.SUBSCRIBED else BotAnswers.SUBSCRIBE_FAIL)
            }

            data.startsWith(State.WAITING_FOR_UNSUBSCRIBE.callbackData!!) -> {
                val animeId = data.removePrefix(State.WAITING_FOR_UNSUBSCRIBE.callbackData).toLong()
                val ok = animeService.unsubscribe(chatId, animeId)

                createEditMessageText(callbackQuery, if (ok) BotAnswers.UNSUBSCRIBED else BotAnswers.UNSUBSCRIBE_FAIL)
            }

            data.startsWith(State.WAITING_FOR_KIND.callbackData!!) -> {
                val kindName = data.removePrefix(State.WAITING_FOR_KIND.callbackData)
                animeService.updateKind(chatId, kindName)

                val genres: List<String> = animeService.findAllGenres()
                val buttons = stringsToButtons(genres, State.WAITING_FOR_GENRE.callbackData)
                createEditMessageText(callbackQuery, "Выберите желаемый жанр", InlineKeyboardMarkup(buttons))
            }

            data.startsWith(State.WAITING_FOR_GENRE.callbackData!!) -> {
                val genreName = data.removePrefix(State.WAITING_FOR_GENRE.callbackData)
                animeService.updateGenre(chatId, genreName)

                val statuses: List<String> = animeService.findAllStatuses()
                val buttons = stringsToButtons(statuses, State.WAITING_FOR_STATUS.callbackData)
                createEditMessageText(callbackQuery, "Выберите желаемый статус", InlineKeyboardMarkup(buttons))
            }

            data.startsWith(State.WAITING_FOR_STATUS.callbackData!!) -> {
                val statusName = data.removePrefix(State.WAITING_FOR_STATUS.callbackData)
                animeService.updateStatus(chatId, statusName)

                val buttons = stringsToButtons(listOf("Да", "Нет"), State.WAITING_FOR_ADDITIONS.callbackData)
                createEditMessageText(callbackQuery, "Что-нибудь еще?", InlineKeyboardMarkup(buttons))
            }

            data.startsWith(State.WAITING_FOR_ADDITIONS.callbackData!!) -> {
                val answer = data.removePrefix(State.WAITING_FOR_ADDITIONS.callbackData)
                if (answer == "Нет") {
                    val animeList: List<AnimeDto> = animeService.getRecommendations(chatId)
                    val buttons = animeToButtons(animeList, data)
                    createEditMessageText(callbackQuery, "Вот список рекомендаций", InlineKeyboardMarkup(buttons))
                } else {
                    userStates[chatId] = State.WAITING_FOR_ADDITIONS
                    createEditMessageText(callbackQuery, "Напишите что-нибудь еще")
                }
            }

            else -> {
                createEditMessageText(
                    callbackQuery,
                    "Неизвестное действие"
                )
            }
        }
    }

    fun processMessage(message: Message): SendMessage {
        val chatId = message.chatId
        val text = message.text.trim()
        val state = userStates.getOrDefault(chatId, State.IDLE)

        return when (state) {
            State.IDLE -> handleCommand(chatId, text)

            State.WAITING_FOR_DETAILS -> {
                userStates[chatId] = State.IDLE
                val matches: List<AnimeDto> = animeService.searchByTitle(text)
                createMessageWithAnimeButtons(matches, chatId, State.WAITING_FOR_DETAILS.callbackData)
            }

            State.WAITING_FOR_SUBSCRIBE -> {
                userStates[chatId] = State.IDLE

                val matches: List<AnimeDto> = animeService.searchByTitle(text)
                createMessageWithAnimeButtons(matches, chatId, State.WAITING_FOR_SUBSCRIBE.callbackData)
            }

            State.WAITING_FOR_UNSUBSCRIBE -> {
                userStates[chatId] = State.IDLE

                val matches: List<AnimeDto> = animeService.searchBySubscribed(chatId)
                createMessageWithAnimeButtons(matches, chatId, State.WAITING_FOR_UNSUBSCRIBE.callbackData)
            }

            State.WAITING_FOR_ADDITIONS -> {
                userStates[chatId] = State.IDLE

                val additionalText = text
                val matches: List<AnimeDto> = animeService.getRecommendations(chatId, additionalText)
                createMessageWithAnimeButtons(matches, chatId, State.WAITING_FOR_DETAILS.callbackData)
            }
            else -> handleCommand(chatId, "")
        }
    }

    private fun handleCommand(chatId: Long, command: String): SendMessage {
        return when (command) {
            Commands.START.command -> {
                createMessage(chatId, BotAnswers.START_MESSAGE)
            }

            // Запросить название аниме, а потом вызвать метод у animeService чтобы отправить сообщение с описанием аниме
            Commands.DETAILS.command -> {
                userStates[chatId] = State.WAITING_FOR_DETAILS
                createMessage(chatId, BotAnswers.ASK_ANIME_TITLE)
            }

            // Запросить название аниме, а потом вызвать метод у animeService для подписки
            Commands.SUBSCRIBE.command -> {
                userStates[chatId] = State.WAITING_FOR_SUBSCRIBE
                createMessage(chatId, BotAnswers.ASK_SUBSCRIBE_TITLE)
            }

            // Получить от animeService список из названий всех аниме на которые есть подписки у данного клиента и потом высветить этот список под сообщением в меню
            Commands.UNSUBSCRIBE.command -> {
                userStates[chatId] = State.WAITING_FOR_UNSUBSCRIBE
                createMessage(chatId, BotAnswers.ASK_UNSUBSCRIBE_TITLE)
            }

            // Вывести сообщение в котором бот спрашивает о типе аниме (сериал/фильм), а затем вызывает метод у animeService с полученной информацией
            Commands.RECOMMENDATIONS.command -> {
                val kinds: List<String> = animeService.findAllKinds()
                createMessageWithButtons(kinds, chatId, State.WAITING_FOR_KIND.callbackData)
            }

            Commands.COMMANDS.command -> {
                createMessage(chatId, BotAnswers.COMMANDS_INFO_MESSAGE)
            }

            else -> {
                createMessage(chatId, BotAnswers.UNKNOWN_COMMAND)
            }
        }
    }

    private fun createMessageWithAnimeButtons(matches: List<AnimeDto>, chatId: Long, callbackData: String?): SendMessage {
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

    private fun animeToButtons(animeList: List<AnimeDto>, callbackData: String? = "") = animeList.map { anime ->
        listOf(
            InlineKeyboardButton().apply {
                this.text = anime.title
                this.callbackData = callbackData + "_${anime.id}"
            }
        )
    }

    private fun stringsToButtons(list: List<String>, callbackData: String? = "") = list.map {
        listOf(
            InlineKeyboardButton().apply {
                this.text = it
                this.callbackData = callbackData + "_${it}"
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
