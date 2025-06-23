package org.example.animeservice.models.dto

data class UserPreferencesDto(
    val chatId: Long,
    val genre: String? = null,
    val kind: String? = null,
    val status: String? = null
)
