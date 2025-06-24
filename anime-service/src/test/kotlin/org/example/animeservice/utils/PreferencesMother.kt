package org.example.animeservice.utils

import org.example.animeservice.models.dto.UserPreferencesDto

object PreferencesMother {
    fun getUserPreferencesDto(
        chatId: Long = 1L,
        genre: String? = "Action",
        kind: String? = "TV",
        status: String? = "Ongoing"
    ): UserPreferencesDto = UserPreferencesDto(
        chatId = chatId,
        genre = genre,
        kind = kind,
        status = status
    )
}
