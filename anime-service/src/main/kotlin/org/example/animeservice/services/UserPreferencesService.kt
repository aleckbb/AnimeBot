package org.example.animeservice.services

import jakarta.transaction.Transactional
import org.example.animeservice.converters.UserPreferencesConverter
import org.example.animeservice.entities.UserPreferencesEntity
import org.example.animeservice.models.dto.UserPreferencesDto
import org.example.animeservice.repositories.UserPreferencesRepository
import org.springframework.stereotype.Service

@Service
class UserPreferencesService(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userPreferencesConverter: UserPreferencesConverter
) {

    @Transactional
    fun updateKind(userId: Long, kind: String) {
        val prefs = userPreferencesRepository.findById(userId)
            .orElse(UserPreferencesEntity(userId))
            .apply { this.kind = kind }
        userPreferencesRepository.save(prefs)
    }

    @Transactional
    fun updateGenre(userId: Long, genre: String) {
        val prefs = userPreferencesRepository.findById(userId)
            .orElse(UserPreferencesEntity(userId))
            .apply { this.genre = genre }
        userPreferencesRepository.save(prefs)
    }

    @Transactional
    fun updateStatus(userId: Long, status: String) {
        val prefs = userPreferencesRepository.findById(userId)
            .orElse(UserPreferencesEntity(userId))
            .apply { this.status = status }
        userPreferencesRepository.save(prefs)
    }

    @Transactional
    fun getUserPreferences(userId: Long): UserPreferencesDto {
        return userPreferencesRepository.findById(userId).map(userPreferencesConverter::toDto)
            .orElse(UserPreferencesDto(userId)).also {
                userPreferencesRepository.deleteById(userId)
            }
    }
}