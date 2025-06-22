package org.example.animeservice.services

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AnimeSubscriptionScheduler(
    private val animeInfoComponent: AnimeInfoComponent,
    private val telegramBotComponent: TelegramBotComponent,
    private val animeService: AnimeService,
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
                animeInfoComponent.updateAnimeInfo(it.getId())
            }
        }
    }
}