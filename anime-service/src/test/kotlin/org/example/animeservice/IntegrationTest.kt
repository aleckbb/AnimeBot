package org.example.animeservice

import org.example.animeservice.repositories.AnimeRepository
import org.example.animeservice.repositories.SubscriptionRepository
import org.example.animeservice.repositories.UserPreferencesRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Testcontainers
abstract class IntegrationTest {

    @Autowired
    protected lateinit var animeRepository: AnimeRepository

    @Autowired
    protected lateinit var subscriptionRepository: SubscriptionRepository

    @Autowired
    protected lateinit var userPreferencesRepository: UserPreferencesRepository

    companion object {

        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:16-alpine").apply {
            withDatabaseName("animedb")
            withUsername("postgres")
            withPassword("postgres")
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun props(reg: DynamicPropertyRegistry) {
            reg.add("spring.datasource.url") { postgres.jdbcUrl }
            reg.add("spring.datasource.username") { postgres.username }
            reg.add("spring.datasource.password") { postgres.password }
        }
    }

    @AfterEach
    fun deleteAll() {
        animeRepository.deleteAll()
        subscriptionRepository.deleteAll()
        userPreferencesRepository.deleteAll()
    }
}
