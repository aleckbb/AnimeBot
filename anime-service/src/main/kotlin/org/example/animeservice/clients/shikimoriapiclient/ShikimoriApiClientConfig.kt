package org.example.animeservice.clients.shikimoriapiclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Configuration
class ShikimoriApiClientConfig {

    @Bean
    fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Bean
    fun okHttpClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    @Bean
    fun retrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://shikimori.one/api/animes")
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Bean
    fun shikimoriApiClient(retrofit: Retrofit): ShikimoriApiClient =
        retrofit.create(ShikimoriApiClient::class.java)
}