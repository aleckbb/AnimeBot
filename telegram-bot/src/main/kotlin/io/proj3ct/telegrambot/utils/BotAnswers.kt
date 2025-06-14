package io.proj3ct.telegrambot.utils

object BotAnswers {
    const val START_MESSAGE = "Добро пожаловать! Я ваш аниме-бот."
    const val UNKNOWN_COMMAND = "Извините, я не понимаю эту команду."
    const val COMMANDS_INFO_MESSAGE = "Доступные команды: /start, /details, /subscribe, /unsubscribe, /recommendations, /commands"

    const val ASK_ANIME_TITLE = "Напишите название аниме для получения подробностей:"
    const val ASK_SUBSCRIBE_TITLE = "Напишите название аниме, на которое хотите подписаться:"
    const val ASK_UNSUBSCRIBE_TITLE = "Напишите название аниме, от которого хотите отписаться:"

    const val SUBSCRIBED = "Вы успешно подписались на обновления этого аниме!"
    const val SUBSCRIBE_FAIL = "Не удалось подписаться. Либо такого аниме нет, либо вы уже подписаны."

    const val UNSUBSCRIBED = "Вы успешно отписались от обновлений этого аниме."
    const val UNSUBSCRIBE_FAIL = "Не удалось отписаться. Либо такого аниме нет, либо вы не были подписаны."
}