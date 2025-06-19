package org.example.animeservice.entities

import jakarta.persistence.*

@Entity
@Table(name = "subscription")
open class SubscriptionEntity(

    @Column(name = "user_id", nullable = false)
    open val userId: Long,

    @Column(name = "anime_id", nullable = false)
    open val animeId: Long
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long? = null
}