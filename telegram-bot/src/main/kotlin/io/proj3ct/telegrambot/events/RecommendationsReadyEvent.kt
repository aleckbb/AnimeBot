package io.proj3ct.telegrambot.events

import org.telegram.telegrambots.meta.api.objects.CallbackQuery

data class RecommendationsReadyEvent(
    val callbackQuery: CallbackQuery,
    val recommendations: String
)