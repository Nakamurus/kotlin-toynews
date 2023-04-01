package com.android.example.toynewsapplication.data.remote.api

import com.android.example.toynewsapplication.data.remote.model.GptRequestBody
import com.android.example.toynewsapplication.data.remote.model.GptResponseBody
import com.android.example.toynewsapplication.data.remote.model.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*


interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

}