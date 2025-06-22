package io.proj3ct.telegrambot.mapper

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.anime.dto.AnimeNameDto

fun AnimeDto.toAnimeNameDto() =  AnimeNameDto(name = title, id = id)

