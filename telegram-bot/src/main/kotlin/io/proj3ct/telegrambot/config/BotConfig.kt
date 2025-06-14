package io.proj3ct.telegrambot.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Configuration
class BotConfig (
    private val telegramBot: LongPollingBot,
) {
    private val logger = LoggerFactory.getLogger("Bot")

    @EventListener(ContextRefreshedEvent::class)
    fun init() {
        try {
            val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
            telegramBotsApi.registerBot(telegramBot)
        } catch (e: TelegramApiException) {
            logger.error("Не удалось создать бота")
        }
    }
}
