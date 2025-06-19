package org.example.animeservice.services

import org.springframework.stereotype.Service

@Service
class AiRecommendationService(
    private val userPreferencesService: UserPreferencesService
) {
}