package com.android.example.toynewsapplication.data.remote.api

import com.android.example.toynewsapplication.data.remote.model.GptRequestBody
import com.android.example.toynewsapplication.data.remote.model.GptResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface GptApiService {
    @Headers("Content-Type: application/json")
    @POST("completions")
    suspend fun generateContext(
        @Header("Authorization") apiKey: String,
        @Body requestBody: GptRequestBody
    ): Response<GptResponseBody>
}