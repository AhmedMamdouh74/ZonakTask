package com.example.data.api

import com.example.data.model.NewsResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface WepServices {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String = "us",
        @Query("category") category: String
    ):Response<NewsResponse?>

    companion object {
        private const val BASE_URL = "https://saurav.tech/NewsAPI/"
        private const val TIME_OUT = 60L
        fun create(): WepServices {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor {
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }.intercept(it)
                }.build()
            val retrofit: WepServices by lazy {
                Retrofit.Builder()
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()
                        )
                    ).baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .build()
                    .create(WepServices::class.java)
            }
            return retrofit
        }
    }
}