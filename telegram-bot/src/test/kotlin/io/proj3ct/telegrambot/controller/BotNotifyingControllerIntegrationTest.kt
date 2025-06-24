package io.proj3ct.telegrambot.controller

import io.proj3ct.anime.dto.UsersAnimeWithNewEpisodesDto
import io.proj3ct.anime.dto.mother.AnimeMother
import io.proj3ct.telegrambot.service.TelegramBot
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import kotlin.test.assertEquals

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
class BotNotifyingControllerIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var rest: TestRestTemplate

    @MockBean
    private lateinit var telegramBot: TelegramBot

    @Test
    fun `should return 204 and invoke telegramBot when posting new episodes`() {
        val dto = UsersAnimeWithNewEpisodesDto(
            userId = 42L,
            animeTitles = listOf(AnimeMother.getAnimeNameDto().name)
        )
        val url = "http://localhost:$port/api/telegram-bot/notify-new-episodes"
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val req = HttpEntity(listOf(dto), headers)

        val resp = rest.postForEntity(url, req, Void::class.java)

        assertEquals(HttpStatus.NO_CONTENT, resp.statusCode)

        verify(telegramBot).notifyUsersAboutNewEpisodes(listOf(dto))
    }
}
