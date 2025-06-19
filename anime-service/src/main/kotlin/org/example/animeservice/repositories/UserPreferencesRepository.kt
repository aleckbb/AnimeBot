package org.example.animeservice.repositories

import org.example.animeservice.entities.UserPreferencesEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserPreferencesRepository: JpaRepository<UserPreferencesEntity, Long> {
}