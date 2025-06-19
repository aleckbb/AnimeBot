package org.example.animeservice.services

import io.proj3ct.anime.dto.AnimeNameDto
import jakarta.transaction.Transactional
import org.example.animeservice.entities.SubscriptionEntity
import org.example.animeservice.repositories.SubscriptionRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
    private val animeService: AnimeService,
    private val animeInfoComponent: AnimeInfoComponent
) {

    /**
     * Подписывает пользователя на аниме.
     * Возвращает true, если подписка была создана, false если уже существует.
     */
    @Transactional
    fun subscribe(userId: Long, animeId: Long): Boolean {
        if (subscriptionRepository.existsByUserIdAndAnimeId(userId, animeId)) {
            return false
        }
        if (!animeService.existsAnime(animeId)) {
            animeInfoComponent.updateAnimeInfo(animeId)
        }
        subscriptionRepository.save(SubscriptionEntity(userId, animeId))
        return true
    }

    /**
     * Отписывает пользователя от аниме.
     * Возвращает true, если запись найдена и удалена, false если её не было.
     */
    @Transactional
    fun unsubscribe(userId: Long, animeId: Long): Boolean {
        val subscription = subscriptionRepository
            .findByUserIdAndAnimeId(userId, animeId)
            ?: return false
        subscriptionRepository.delete(subscription)
        return true
    }

    /**
     * Ищет все аниме, на которые подписан пользователь,
     * и возвращает их ID и заголовки.
     */
    @Transactional
    fun searchBySubscribed(userId: Long): List<AnimeNameDto> {
        return subscriptionRepository.findAllByUserId(userId)
            .map { sub ->
                val title = animeService.findTitleById(sub.animeId)
                AnimeNameDto(
                    id = sub.animeId,
                    name = title
                )
            }
    }
}