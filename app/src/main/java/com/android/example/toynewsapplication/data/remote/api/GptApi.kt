package com.android.example.toynewsapplication.data.remote.api

import com.android.example.toynewsapplication.data.remote.model.GptRequestBody
import com.android.example.toynewsapplication.data.remote.model.GptResponseBody
import retrofit2.Response
import retrofit2.http.*

interface GptApiService{
    @Headers("Content-Type: application/json")
    @POST("completions")
    suspend fun generateContext(
        @Body requestBody: GptRequestBody
    ): Response<GptResponseBody>
}