package org.example.animeservice.services

import jakarta.transaction.Transactional
import org.example.animeservice.entities.UserPreferencesEntity
import org.example.animeservice.repositories.UserPreferencesRepository
import org.springframework.stereotype.Service

@Service
class UserPreferencesService(
    private val userPreferencesRepository: UserPreferencesRepository
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
    fun getUserPreferences(userId: Long): UserPreferencesEntity {
        return userPreferencesRepository.findById(userId).orElse(UserPreferencesEntity(userId))
    }
}