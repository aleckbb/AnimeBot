package org.example.animeservice.converters

import org.example.animeservice.entities.UserPreferencesEntity
import org.example.animeservice.models.dto.UserPreferencesDto
import org.springframework.stereotype.Component

@Component
class UserPreferencesConverter {

    fun toDto(entity: UserPreferencesEntity): UserPreferencesDto {
        return UserPreferencesDto(
            entity.userId,
            entity.genre,
            entity.kind,
            entity.status
        )
    }
}