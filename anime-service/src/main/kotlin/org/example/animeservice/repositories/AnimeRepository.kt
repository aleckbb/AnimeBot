package org.example.animeservice.repositories

import org.example.animeservice.entities.AnimeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface AnimeRepository: JpaRepository<AnimeEntity, Long> {

    @Query(
        """
            select a.userIds as userIds, 
                case 
                    when a.russian is not null and trim(a.russian) <> ''
                    then a.russian 
                    else a.name
                end as title,
                a.id as id
            from AnimeEntity a
            where a.status = 'ongoing'
                and a.userIds is not empty 
                and a.nextEpisodeAt is not null
                and a.nextEpisodeAt < :dateTime
        """
    )
    fun findTitleAndSubsByNextEpisodeAt(
        dateTime: LocalDateTime
    ): List<AnimeUserIds>
}

interface AnimeUserIds {
    fun getUserIds(): Set<Long>
    fun getTitle(): String
    fun getId(): Long
}