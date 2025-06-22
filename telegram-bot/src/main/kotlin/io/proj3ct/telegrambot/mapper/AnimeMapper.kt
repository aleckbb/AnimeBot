package io.proj3ct.telegrambot.mapper

import io.proj3ct.anime.dto.AnimeDto
import io.proj3ct.anime.dto.AnimeNameDto

fun AnimeDto.toAnimeNameDto() = AnimeNameDto(name = getTitle(this), id = id)

private fun getTitle(anime: AnimeDto): String {
    return anime.russian.ifEmpty { anime.name }
}

fun AnimeDto.toFullStringInfo() = buildString {
    appendLine("🎬 ${russian} (${name})")
    appendLine("• Тип: $kind")
    appendLine("• Статус: $status")
    appendLine("• Серии: $episodes")
    appendLine("• Оценка: $score")
    appendLine("• Рейтинг: ${rating ?: "—"}")
    appendLine("• Длительность: ${duration?.let { "$it мин." } ?: "—"}")
    appendLine("• Дата премьеры: ${airedOn ?: "—"}")
    appendLine("• Дата выпуска: ${releasedOn ?: "—"}")
    if (nextEpisodeAt != null) {
        appendLine("• След. серия: $nextEpisodeAt")
    }
    appendLine("• Жанры: ${genres?.joinToString(", ") ?: "—"}")
    appendLine("• Студии: ${studios?.joinToString(", ") ?: "—"}")
    appendLine()
    appendLine("Описание:")
    appendLine(description?.takeIf { it.isNotBlank() } ?.cleanDescription() ?: "Нет описания")
    appendLine()
    appendLine("🔗 Ссылка: https://shikimori.one$url")
}

private fun String.cleanDescription(): String =
    replace(Regex("""\[character=\d+](.*?)\[/character]"""), "$1")
        .replace(Regex("""[ \t]{2,}"""), " ")
        .lines()
        .joinToString("\n") { it.trim() }
        .trim()