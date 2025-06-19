package org.example.animeservice.clients.shikimoriapiclient

import org.example.animeservice.models.json.AnimeCompactJson
import org.example.animeservice.models.json.AnimeJson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

@Component
class ShikimoriWebClient(
    private val shikimoriClient: WebClient
) {

    /**
     * Получить полную информацию об аниме по ID.
     */
    suspend fun getAnimeInfo(id: Long): AnimeJson? {
        return try {
            shikimoriClient.get()
                .uri("/{id}", id)
                .retrieve()
                .awaitBody<AnimeJson>()
        } catch (e: WebClientResponseException) {
            log.error("Ошибка при запросе anime/$id: ${e.rawStatusCode} ${e.responseBodyAsString}", e)
            null
        } catch (e: Exception) {
            log.error("Неожиданная ошибка при getAnimeInfo", e)
            null
        }
    }

    /**
     * Поиск аниме по названию.
     */
    suspend fun searchAnime(
        query: String,
        limit: Int = 10,
        page: Int = 1
    ): List<AnimeCompactJson> {
        return try {
            shikimoriClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .queryParam("search", query)
                        .queryParam("limit", limit)
                        .queryParam("page", page)
                        .build()
                }
                .retrieve()
                .awaitBody()
        } catch (e: WebClientResponseException) {
            log.error("Ошибка при поиске anime?search=$query: ${e.rawStatusCode}", e)
            emptyList()
        } catch (e: Exception) {
            log.error("Неожиданная ошибка при searchAnime", e)
            emptyList()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ShikimoriWebClient::class.java)
    }
}