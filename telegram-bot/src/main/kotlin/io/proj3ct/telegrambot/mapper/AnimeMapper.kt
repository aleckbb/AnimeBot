package io.proj3ct.telegrambot.mapper

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.anime.dto.AnimeNameDto

fun AnimeDto.toAnimeNameDto() = AnimeNameDto(name = getTitle(this), id = id)

private fun getTitle(anime: AnimeDto): String {
    return if(anime.russian.isEmpty()) anime.russian
    else anime.name
}

