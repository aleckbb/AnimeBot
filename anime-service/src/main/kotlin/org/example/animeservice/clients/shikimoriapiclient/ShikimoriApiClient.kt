package org.example.animeservice.clients.shikimoriapiclient

import org.example.animeservice.models.json.AnimeJson
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShikimoriApiClient {

    @GET("{id}")
    suspend fun getAnimeInfo(@Path("id") id: Int): AnimeJson

    @GET("")
    suspend fun searchAnimes(
        @Query("search") query: String,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1
    ): List<AnimeJson>
}