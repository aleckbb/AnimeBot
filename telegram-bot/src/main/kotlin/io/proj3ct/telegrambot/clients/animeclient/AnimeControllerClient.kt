package io.proj3ct.telegrambot.clients.animeclient

import io.proj3ct.anime.dto.AnimeDto
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AnimeControllerClient(
    private val webClient: WebClient
) {

    /* ---------- Аниме-карточка ---------- */

    fun getDetailsById(id: Long): AnimeDto? =
        webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .bodyToMono(AnimeDto::class.java)
            .block()

    /* ---------- Подписки ---------- */

    fun subscribe(chatId: Long, animeId: Long): Boolean =
        webClient.post()
            .uri("/{id}/subscribe?chatId={chatId}", animeId, chatId)
            .retrieve()
            .toBodilessEntity()
            .map { it.statusCode.is2xxSuccessful }
            .block() ?: false

    fun unsubscribe(chatId: Long, animeId: Long): Boolean =
        webClient.delete()
            .uri("/{id}/unsubscribe?chatId={chatId}", animeId, chatId)
            .retrieve()
            .toBodilessEntity()
            .map { it.statusCode.is2xxSuccessful }
            .block() ?: false

    /* ---------- Предпочтения пользователя ---------- */

    fun updateKind(chatId: Long, kind: String) =
        webClient.put()
            .uri("/preferences/{chatId}/kind?kind={kind}", chatId, kind)
            .retrieve()
            .toBodilessEntity()
            .block()

    fun updateGenre(chatId: Long, genre: String) =
        webClient.put()
            .uri("/preferences/{chatId}/genre?genre={genre}", chatId, genre)
            .retrieve()
            .toBodilessEntity()
            .block()

    fun updateStatus(chatId: Long, status: String) =
        webClient.put()
            .uri("/preferences/{chatId}/status?status={status}", chatId, status)
            .retrieve()
            .toBodilessEntity()
            .block()

    /* ---------- Справочные данные ---------- */

    fun findAllKinds(): List<String> =
        webClient.get()
            .uri("/kinds")
            .retrieve()
            .bodyToFlux(String::class.java)
            .collectList()
            .block() ?: emptyList()

    fun findAllGenres(): List<String> =
        webClient.get()
            .uri("/genres")
            .retrieve()
            .bodyToFlux(String::class.java)
            .collectList()
            .block() ?: emptyList()

    fun findAllStatuses(): List<String> =
        webClient.get()
            .uri("/statuses")
            .retrieve()
            .bodyToFlux(String::class.java)
            .collectList()
            .block() ?: emptyList()

    /* ---------- Поиск и рекомендации ---------- */

    fun searchByTitle(query: String): List<AnimeDto> =
        webClient.get()
            .uri("/search?title={title}", query)
            .retrieve()
            .bodyToFlux(AnimeDto::class.java)
            .collectList()
            .block() ?: emptyList()

    fun searchBySubscribed(chatId: Long): List<AnimeDto> =
        webClient.get()
            .uri("/subscribed/{chatId}", chatId)
            .retrieve()
            .bodyToFlux(AnimeDto::class.java)
            .collectList()
            .block() ?: emptyList()

    fun getRecommendations(chatId: Long, additionalText: String? = ""): List<AnimeDto> =
        webClient.get()
            .uri("/recommendations/{chatId}", chatId, additionalText)
            .retrieve()
            .bodyToFlux(AnimeDto::class.java)
            .collectList()
            .block() ?: emptyList()
}
