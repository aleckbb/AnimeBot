package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeNameDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.animeservice.clients.gptapiclient.GptWebClient
import org.example.animeservice.models.dto.UserPreferencesDto
import org.springframework.stereotype.Service

@Service
class AiRecommendationService(
    private val userPreferencesService: UserPreferencesService,
    private val gptWebClient: GptWebClient
) {

    suspend fun getRecommendations(chatId: Long, additionalText: String): String {
        val userPreferences = withContext(Dispatchers.IO) {
            userPreferencesService.getUserPreferences(chatId)
        }
        return gptWebClient.ask(prepareQuery(userPreferences, additionalText))

    }

    private fun prepareQuery(userPreferencesDto: UserPreferencesDto, additionalText: String): String {
        val prompt = buildString {
            appendLine("Порекомендуй мне 5 аниме, которые мне понравятся по следующим критериям")
            if (!userPreferencesDto.genre.isNullOrBlank()) {
                appendLine("- Жанр: ${userPreferencesDto.genre}")
            }
            if (!userPreferencesDto.kind.isNullOrBlank()) {
                appendLine("- Тип: ${userPreferencesDto.kind}")
            }
            if (!userPreferencesDto.status.isNullOrBlank()) {
                appendLine("- Статус: ${userPreferencesDto.status}")
            }
            appendLine("- Пожелания: ").append(additionalText)
            appendLine("Ты – бот, рекомендующий аниме.")
            appendLine("Верни **ТОЛЬКО** нумерованный список из названий аниме, без комментариев, без текстовых пояснений, без обёртки в ``` или markdown.")
            appendLine("Ответ ТОЛЬКО на русском языке")
        }
        return prompt
    }
}