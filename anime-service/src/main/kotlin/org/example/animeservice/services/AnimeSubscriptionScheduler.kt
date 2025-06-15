package org.example.animeservice.services

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AnimeSubscriptionScheduler(
    private val actualInfoProcessComponent: ActualInfoProcessComponent,
    private val telegramBotComponent: TelegramBotComponent,
    private val animeService: AnimeServiceImpl,
) {

    @Scheduled(cron = "\${app.schedule.cron}")
    fun checkNewEpisodes() {
        val animeTitles = animeService.findAnimeWithNewEpisodes()
        if (animeTitles.isEmpty()) {
            return;
        } else {
            val userAnimeList = animeTitles.asSequence()
                .flatMap { record ->
                    record.getUserIds().map { userId ->
                        userId to record.getTitle()
                    }
                }.groupBy(
                    { it.first }, { it.second }
                ).map { (userId, titles) ->
                    UsersAnimeWithNewEpisodesDto(userId, titles)
                }
            telegramBotComponent.notifyUsersAboutNewEpisodes(userAnimeList)
            animeTitles.forEach {
                actualInfoProcessComponent.updateAnimeInfo(it.getId())
            }
        }
    }
}