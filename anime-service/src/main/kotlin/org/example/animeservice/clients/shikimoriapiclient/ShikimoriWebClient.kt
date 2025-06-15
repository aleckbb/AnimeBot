package org.example.animeservice.clients.shikimoriapiclient

import org.example.animeservice.models.json.AnimeCompactJson
import org.example.animeservice.models.json.AnimeJson
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class ShikimoriWebClient(
    private val shikimoriWebClient: WebClient
) {

    /**
     * Получить полную информацию об аниме по ID.
     */
    suspend fun getAnimeInfo(id: Int): AnimeJson =
        shikimoriWebClient.get()
            .uri("/{id}", id)
            .retrieve()
            .awaitBody()

    /**
     * Поиск аниме по названию.
     */
    suspend fun searchAnime(
        query: String,
        limit: Int = 20,
        page: Int = 1
    ): List<AnimeCompactJson> =
        shikimoriWebClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .queryParam("search", query)
                    .queryParam("limit", limit)
                    .queryParam("page", page)
                    .build()
            }
            .retrieve()
            .awaitBody()
}