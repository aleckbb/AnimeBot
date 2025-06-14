package io.proj3ct.telegrambot.service

import io.proj3ct.telegrambot.config.BotProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
open class TelegramBot : TelegramLongPollingBot() {

    private lateinit var botProperties: BotProperties

    //    private lateinit var animeService: AnimeService
    private val logger = LoggerFactory.getLogger("Bot")

    @Autowired
    fun TelegramBot(botProperties: BotProperties) {
        this.botProperties = botProperties

        val commands: MutableList<BotCommand> = ArrayList()
        commands.add(BotCommand("/start", "начать общение"))
        commands.add(BotCommand("/recommendations", "порекомендовать аниме"))
        commands.add(BotCommand("/details", "получить информацию об аниме"))
        commands.add(BotCommand("/subscribe", "уведомлять о выходе новых серий"))
        commands.add(BotCommand("/unsubscribe", "не уведомлять о новых сериях"))
        commands.add(BotCommand("/commands", "получить список доступных команд"))
        try {
            execute(SetMyCommands(commands, BotCommandScopeDefault(), null))
        } catch (e: TelegramApiException) {
            logger.error("Ошибка при задании списка команд: " + e.message)
        }
    }

    override fun getBotToken() = botProperties.token

    override fun getBotUsername() = botProperties.name

    override fun onUpdateReceived(p0: Update?) {
        try {
//            execute(animeService.processUpdate(update))
        } catch (e: TelegramApiException) {
            logger.error("Ошибка при ответе пользователю!")
        }
    }
}