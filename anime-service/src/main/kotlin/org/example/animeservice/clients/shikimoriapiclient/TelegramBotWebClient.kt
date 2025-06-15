package org.example.animeservice.clients.shikimoriapiclient

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException

@Component
class TelegramBotWebClient(
    private val telegramBotClient: WebClient
) {

    fun notifyUsersAboutNewEpisodes(
        userEpisodes: List<UsersAnimeWithNewEpisodesDto>
    ): Boolean {
        return try {
            telegramBotClient.post()
                .uri("/notify-new-episodes")
                .bodyValue(userEpisodes)
                .retrieve()
                .toBodilessEntity()
                .map { it.statusCode.is2xxSuccessful }
                .block() ?: false
        } catch (e: WebClientResponseException) {
            log.error(
                "Ошибка при уведомлении /notify-new-episodes: " +
                        "${e.rawStatusCode} ${e.responseBodyAsString}", e
            )
            false
        } catch (e: Exception) {
            log.error("Неожиданная ошибка в notifyUsersAboutNewEpisodes", e)
            false
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ShikimoriWebClient::class.java)
    }
}