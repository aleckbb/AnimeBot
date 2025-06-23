package org.example.animeservice.controllers

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.anime.dto.AnimeNameDto
import org.example.animeservice.services.AiRecommendationService
import org.example.animeservice.services.AnimeService
import org.example.animeservice.services.SubscriptionService
import org.example.animeservice.services.UserPreferencesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/anime")
class AnimeController @Autowired constructor(
    private val animeService: AnimeService,
    private val subscriptionService: SubscriptionService,
    private val aiRecommendationService: AiRecommendationService,
    private val userPreferencesService: UserPreferencesService
) {

    /** Детали по ID */
    @GetMapping("/{id}")
    suspend fun getDetailsById(@PathVariable id: Long): ResponseEntity<AnimeDto> =
        animeService.getDetailsById(id).let { ResponseEntity.ok(it) }

    /** Подписка на аниме */
    @PostMapping("/{id}/subscribe")
    suspend fun subscribe(
        @PathVariable id: Long,
        @RequestParam chatId: Long
    ): ResponseEntity<Unit> =
        if (subscriptionService.subscribe(chatId, id))
            ResponseEntity.ok().build()
        else
            ResponseEntity.badRequest().build()

    /** Отписка от аниме */
    @DeleteMapping("/{id}/unsubscribe")
    fun unsubscribe(
        @PathVariable id: Long,
        @RequestParam chatId: Long
    ): ResponseEntity<Unit> =
        if (subscriptionService.unsubscribe(chatId, id))
            ResponseEntity.ok().build()
        else
            ResponseEntity.badRequest().build()

    /** Обновить тип (kind) */
    @PutMapping("/preferences/{chatId}/kind")
    fun updateKind(
        @PathVariable chatId: Long,
        @RequestParam kind: String
    ): ResponseEntity<Unit> {
        userPreferencesService.updateKind(chatId, kind)
        return ResponseEntity.noContent().build()
    }

    /** Обновить жанр */
    @PutMapping("/preferences/{chatId}/genre")
    fun updateGenre(
        @PathVariable chatId: Long,
        @RequestParam genre: String
    ): ResponseEntity<Unit> {
        userPreferencesService.updateGenre(chatId, genre)
        return ResponseEntity.noContent().build()
    }

    /** Обновить статус */
    @PutMapping("/preferences/{chatId}/status")
    fun updateStatus(
        @PathVariable chatId: Long,
        @RequestParam status: String
    ): ResponseEntity<Unit> {
        userPreferencesService.updateStatus(chatId, status)
        return ResponseEntity.noContent().build()
    }

    /** Все доступные типы */
    @GetMapping("/kinds")
    fun findAllKinds(): ResponseEntity<List<String>> =
        ResponseEntity.ok(animeService.findAllKinds())

    /** Все жанры */
    @GetMapping("/genres")
    fun findAllGenres(): ResponseEntity<List<String>> =
        ResponseEntity.ok(animeService.findAllGenres())

    /** Все статусы */
    @GetMapping("/statuses")
    fun findAllStatuses(): ResponseEntity<List<String>> =
        ResponseEntity.ok(animeService.findAllStatuses())

    /** Поиск по названию */
    @GetMapping("/search")
    suspend fun searchByTitle(@RequestParam title: String): ResponseEntity<List<AnimeNameDto>> =
        ResponseEntity.ok(animeService.searchByTitle(title))

    /** Список подписок пользователя */
    @GetMapping("/subscribed/{chatId}")
    fun searchBySubscribed(@PathVariable chatId: Long): ResponseEntity<List<AnimeNameDto>> =
        ResponseEntity.ok(subscriptionService.searchBySubscribed(chatId))

    /** Рекомендации для пользователя */
    @GetMapping("/recommendations/{chatId}")
    suspend fun getRecommendations(@PathVariable chatId: Long, additionalText: String = ""): ResponseEntity<String> =
        ResponseEntity.ok(aiRecommendationService.getRecommendations(chatId, additionalText))
}
