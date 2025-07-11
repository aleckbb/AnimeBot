package io.proj3ct.telegrambot.service

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import io.proj3ct.telegrambot.config.BotProperties
import io.proj3ct.telegrambot.events.RecommendationsReadyEvent
import io.proj3ct.telegrambot.utils.BotAnswers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class TelegramBot : TelegramLongPollingBot() {

    private lateinit var botProperties: BotProperties
    private lateinit var messageService: MessageService
    private val logger = LoggerFactory.getLogger("Bot")

    @Autowired
    fun TelegramBot(botProperties: BotProperties, messageService: MessageService) {
        this.botProperties = botProperties
        this.messageService = messageService
        val commands: List<BotCommand> = Commands.entries.map { BotCommand(it.command, it.description) }
        try {
            execute(SetMyCommands(commands, BotCommandScopeDefault(), null))
        } catch (e: TelegramApiException) {
            logger.error("Ошибка при задании списка команд: " + e.message)
        }
    }

    override fun getBotToken() = botProperties.token

    override fun getBotUsername() = botProperties.name

    override fun onUpdateReceived(update: Update) {
        try {
            when {
                update.hasCallbackQuery() ->
                    execute(messageService.processCallbackQuery(update.callbackQuery))

                update.hasMessage() && update.message.hasText() ->
                    execute(messageService.processMessage(update.message))
            }
        } catch (e: TelegramApiException) {
            logger.error("Ошибка при ответе пользователю!")
        }
    }

    fun notifyUsersAboutNewEpisodes(userEpisodes: List<UsersAnimeWithNewEpisodesDto>) {
        userEpisodes.forEach { ue ->
            val text = buildString {
                append("Новые серии для следующих аниме:\n")
                ue.animeTitles.forEach { anime ->
                    append("• ${anime}\n")
                }
            }
            try {
                val msg = SendMessage(ue.userId.toString(), text)
                execute(msg)
                logger.info("Notified chat ${ue.userId} with ${ue.animeTitles.size} anime entries")
            } catch (e: TelegramApiException) {
                logger.error("Не удалось отправить уведомление chat=${ue.userId}", e)
            }
        }
    }

    @EventListener
    fun onRecommendationsReady(event: RecommendationsReadyEvent) {
        val text = if (event.recommendations.isEmpty()) {
            "Нет рекомендаций"
        } else { event.recommendations
        }
        val edit = EditMessageText()
            .apply {
                chatId    = event.callbackQuery.message.chatId.toString()
                messageId = event.callbackQuery.message.messageId
                this.text = BotAnswers.RECOMMENDATIONS_INTRO_MESSAGE + text
            }

        execute(edit)
    }

    enum class Commands(val command: String, val description: String) {
        START("/start", "начать общение"),
        RECOMMENDATIONS("/recommendations", "порекомендовать аниме"),
        DETAILS("/details", "получить информацию об аниме"),
        SUBSCRIBE("/subscribe", "уведомлять о выходе новых серий"),
        UNSUBSCRIBE("/unsubscribe", "не уведомлять о новых сериях"),
        COMMANDS("/commands", "получить список доступных команд")
    }
}