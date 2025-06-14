package io.proj3ct.telegrambot.service

import io.proj3ct.telegrambot.service.TelegramBot.Commands
import io.proj3ct.telegrambot.utils.BotAnswers
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class MessageService {

    fun processCallbackQuery(callbackQuery: CallbackQuery): EditMessageText {
        val chatId = callbackQuery.message.chatId.toString()
        val messageId = callbackQuery.message.messageId
        val data = callbackQuery.data

        return EditMessageText().apply {
            this.chatId = chatId
            this.messageId = messageId
            this.text = "Вы нажали кнопку: $data"
        }
    }

    fun processMessage(message: Message): SendMessage {
        val receivedText = message.text
        val chatId = message.chatId

        return when (receivedText) {
            Commands.START.command -> {
                createMessage(chatId, BotAnswers.START_MESSAGE)
            }

            Commands.COMMANDS.command -> {
                createMessage(chatId, BotAnswers.COMMANDS_INFO_MESSAGE)
            }

            else -> {
                createMessage(chatId, BotAnswers.UNKNOWN_COMMAND)
            }
        }
    }

    private fun createMessage(chatId: Long, text: String): SendMessage {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = text
        return message
    }
}
