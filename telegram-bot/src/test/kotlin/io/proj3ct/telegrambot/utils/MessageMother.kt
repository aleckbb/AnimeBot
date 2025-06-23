package io.proj3ct.telegrambot.utils

import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message

object MessageMother {
    fun getMessage(text: String): Message = Message().apply {
        this.text = text
        this.chat = Chat().apply { id = TestData.CHAT_ID }
    }

    fun getCallback(data: String): CallbackQuery {
        val message = Message().apply {
            messageId = TestData.MESSAGE_ID
            this.chat = Chat().apply { id = TestData.CHAT_ID }
        }
        return CallbackQuery().apply {
            this.data = data
            this.message = message
        }
    }
}