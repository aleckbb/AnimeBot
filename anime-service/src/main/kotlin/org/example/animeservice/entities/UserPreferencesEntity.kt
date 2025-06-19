package org.example.animeservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_preferences")
open class UserPreferencesEntity(

    @Id
    @Column(name = "user_id", nullable = false)
    open val userId: Long
) {

    @Column(name = "genre")
    open var genre: String? = null

    @Column(name = "kind")
    open var kind: String? = null

    @Column(name = "status")
    open var status: String? = null
}