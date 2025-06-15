package org.example.animeservice.services

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AnimeSubscriptionScheduler(
    private val actualInfoProcessComponent: ActualInfoProcessComponent,
) {

    @Scheduled(cron = "\${app.schedule.cron}")
    fun checkNewEpisodes() {

        actualInfoProcessComponent.updateAnimeInfo(213)
    }
}