package org.example.animeservice.clients.gptapiclient

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.proj3ct.anime.dto.AnimeNameDto
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withTimeout
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
class GptWebClient(
    private val gptClient: WebClient
) {

    suspend fun ask(question: String): String {
        return withTimeout(20_000) {
            gptClient.get()
                .uri { uriBuilder -> uriBuilder.queryParam("text", question).build() }
                .retrieve()
                .bodyToMono(String::class.java)
                .awaitSingle()
                .trim()
        }
    }
}