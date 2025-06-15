package org.example.animeservice.repositories

import org.example.animeservice.entities.AnimeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AnimeRepository: JpaRepository<AnimeEntity, Long> {
}