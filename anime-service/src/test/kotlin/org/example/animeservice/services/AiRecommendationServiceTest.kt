package org.example.animeservice.services

import kotlinx.coroutines.runBlocking
import org.example.animeservice.clients.gptapiclient.GptWebClient
import org.example.animeservice.utils.PreferencesMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class AiRecommendationServiceTest {

    @Mock
    private lateinit var prefsSvc: UserPreferencesService

    @Mock
    private lateinit var gptWebClient: GptWebClient

    @InjectMocks
    private lateinit var svc: AiRecommendationService

    @Test
    fun `when getRecommendations then build prompt with all preferences`() {
        runBlocking {
            val chatId = 123L
            val prefs = PreferencesMother.getUserPreferencesDto(
                chatId = chatId,
                genre = "Action",
                kind = "TV",
                status = "Ongoing"
            )
            whenever(prefsSvc.getUserPreferences(chatId)).thenReturn(prefs)
            whenever(gptWebClient.ask(any())).thenReturn("RESULT")

            val result = svc.getRecommendations(chatId, "Mecha")
            assertEquals("RESULT", result)

            val captor = argumentCaptor<String>()
            verify(gptWebClient).ask(captor.capture())
            val prompt = captor.firstValue

            assertTrue(prompt.contains("- Жанр: Action"), "должен содержать жанр")
            assertTrue(prompt.contains("- Тип: TV"), "должен содержать тип")
            assertTrue(prompt.contains("- Статус: Ongoing"), "должен содержать статус")
            assertTrue(prompt.contains("- Пожелания:"), "должен содержать строку пожеланий")
            assertTrue(prompt.contains("Mecha"), "должен содержать дополнительный текст")
        }
    }

    @Test
    fun `when preferences missing then skip empty lines in prompt`() {
        runBlocking {
            val chatId = 456L
            val prefs = PreferencesMother.getUserPreferencesDto(
                chatId = chatId,
                genre = null,
                kind = "",
                status = null
            )
            whenever(prefsSvc.getUserPreferences(chatId)).thenReturn(prefs)
            whenever(gptWebClient.ask(any())).thenReturn("OK")

            svc.getRecommendations(chatId, "ExtraText")

            val captor = argumentCaptor<String>()
            verify(gptWebClient).ask(captor.capture())
            val prompt = captor.firstValue

            assertFalse(prompt.contains("- Жанр:"), "не должно быть строки про жанр")
            assertFalse(prompt.contains("- Тип:"), "не должно быть строки про тип")
            assertFalse(prompt.contains("- Статус:"), "не должно быть строки про статус")
            assertTrue(prompt.contains("- Пожелания:"), "должно быть строка про пожелания")
            assertTrue(prompt.contains("ExtraText"), "должен содержать дополнительный текст")
        }
    }

    @Test
    fun `when delegate to GptWebClient then return its answer`() {
        runBlocking {
            val chatId = 789L
            val prefs = PreferencesMother.getUserPreferencesDto(chatId = chatId)
            whenever(prefsSvc.getUserPreferences(chatId)).thenReturn(prefs)
            whenever(gptWebClient.ask(any())).thenReturn("AI_RESPONSE")

            val response = svc.getRecommendations(chatId, "")

            assertEquals("AI_RESPONSE", response, "должен вернуть строку от GPT-клиента")
            verify(gptWebClient).ask(any())
        }
    }
}
