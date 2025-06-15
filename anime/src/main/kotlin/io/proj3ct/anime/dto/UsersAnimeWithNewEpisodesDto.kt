package io.proj3ct.anime.dto

data class UsersAnimeWithNewEpisodesDto(
    val userId: Long,
    val animeTitles: List<String>
)