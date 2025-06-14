package io.proj3ct.telegrambot.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "bot")
open class BotProperties (
    val name: String,
    val token: String
)