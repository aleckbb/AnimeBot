package org.example.animeservice.repositories

import org.example.animeservice.entities.SubscriptionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionRepository: JpaRepository<SubscriptionEntity, Long> {

    fun existsByUserIdAndAnimeId(userId: Long, animeId: Long): Boolean

    fun findByUserIdAndAnimeId(userId: Long, animeId: Long): SubscriptionEntity?

    fun findAllByUserId(userId: Long): List<SubscriptionEntity>
}