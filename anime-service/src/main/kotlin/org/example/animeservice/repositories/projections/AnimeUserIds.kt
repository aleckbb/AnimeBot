package org.example.animeservice.repositories.projections

interface AnimeUserIds {
    fun getUserIds(): Set<Long>
    fun getTitle(): String
    fun getId(): Long
}