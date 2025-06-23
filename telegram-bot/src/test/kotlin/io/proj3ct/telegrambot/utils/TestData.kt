package io.proj3ct.telegrambot.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object TestData {

    const val CHAT_ID = 1L
    const val MESSAGE_ID = 1
    val LOCAL_DATE_TIME = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
    val LOCAL_DATE = LocalDate.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
    val TITLE = "Стальной алхимик: Братство"

}