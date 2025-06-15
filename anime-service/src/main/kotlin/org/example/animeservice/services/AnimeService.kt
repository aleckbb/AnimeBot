package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeDto


/**
 * Сервис‑заглушка. Пока что возвращает пустые данные, чтобы
 * приложение могло компилироваться и интеграционные точки
 * Telegram‑бота работали без ошибок.
 */
interface AnimeService {
    fun getDetailsById(id: Long): AnimeDto?
    fun subscribe(chatId: Long, animeId: Long): Boolean
    fun unsubscribe(chatId: Long, animeId: Long): Boolean

    fun updateKind(chatId: Long, kind: String)
    fun updateGenre(chatId: Long, genre: String)
    fun updateStatus(chatId: Long, status: String)

    fun findAllKinds(): List<String>
    fun findAllGenres(): List<String>
    fun findAllStatuses(): List<String>

    fun searchByTitle(query: String): List<AnimeDto>
    fun searchBySubscribed(chatId: Long): List<AnimeDto>
    fun getRecommendations(chatId: Long, additionalText: String = ""): List<AnimeDto>
}
