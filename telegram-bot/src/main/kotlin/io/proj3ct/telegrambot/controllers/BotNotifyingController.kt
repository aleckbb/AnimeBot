package io.proj3ct.telegrambot.controllers

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import io.proj3ct.telegrambot.service.TelegramBot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/telegram-bot")
class BotNotifyingController @Autowired constructor(
    private val telegramBot: TelegramBot,
) {

    @PostMapping("/notify-new-episodes")
    fun notifyUsersAboutNewEpisodes(
        @RequestBody userEpisodes: List<UsersAnimeWithNewEpisodesDto>
    ): ResponseEntity<Unit> {
        telegramBot.notifyUsersAboutNewEpisodes(userEpisodes)
        return ResponseEntity.noContent().build()
    }
}
