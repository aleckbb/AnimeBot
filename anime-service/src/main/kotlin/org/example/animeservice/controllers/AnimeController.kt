package org.example.animeservice.controllers

import io.proj3ct.anime.dto.AnimeDto
import org.example.animeservice.services.AnimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/anime")
class AnimeController @Autowired constructor(
    private val animeService: AnimeService
) {

    /** Детали по ID */
    @GetMapping("/{id}")
    fun getDetailsById(@PathVariable id: Long): ResponseEntity<AnimeDto> =
        animeService.getDetailsById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    /** Подписка на аниме */
    @PostMapping("/{id}/subscribe")
    fun subscribe(
        @PathVariable id: Long,
        @RequestParam chatId: Long
    ): ResponseEntity<Unit> =
        if (animeService.subscribe(chatId, id))
            ResponseEntity.ok().build()
        else
            ResponseEntity.badRequest().build()

    /** Отписка от аниме */
    @DeleteMapping("/{id}/unsubscribe")
    fun unsubscribe(
        @PathVariable id: Long,
        @RequestParam chatId: Long
    ): ResponseEntity<Unit> =
        if (animeService.unsubscribe(chatId, id))
            ResponseEntity.ok().build()
        else
            ResponseEntity.badRequest().build()

    /** Обновить тип (kind) */
    @PutMapping("/preferences/{chatId}/kind")
    fun updateKind(
        @PathVariable chatId: Long,
        @RequestParam kind: String
    ): ResponseEntity<Unit> {
        animeService.updateKind(chatId, kind)
        return ResponseEntity.noContent().build()
    }

    /** Обновить жанр */
    @PutMapping("/preferences/{chatId}/genre")
    fun updateGenre(
        @PathVariable chatId: Long,
        @RequestParam genre: String
    ): ResponseEntity<Unit> {
        animeService.updateGenre(chatId, genre)
        return ResponseEntity.noContent().build()
    }

    /** Обновить статус */
    @PutMapping("/preferences/{chatId}/status")
    fun updateStatus(
        @PathVariable chatId: Long,
        @RequestParam status: String
    ): ResponseEntity<Unit> {
        animeService.updateStatus(chatId, status)
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
    fun searchByTitle(@RequestParam q: String): ResponseEntity<List<AnimeDto>> =
        ResponseEntity.ok(animeService.searchByTitle(q))

    /** Список подписок пользователя */
    @GetMapping("/subscribed/{chatId}")
    fun searchBySubscribed(@PathVariable chatId: Long): ResponseEntity<List<AnimeDto>> =
        ResponseEntity.ok(animeService.searchBySubscribed(chatId))

    /** Рекомендации для пользователя */
    @GetMapping("/recommendations/{chatId}")
    fun getRecommendations(@PathVariable chatId: Long, additionalText: String = ""): ResponseEntity<List<AnimeDto>> =
        ResponseEntity.ok(animeService.getRecommendations(chatId, additionalText))
}
