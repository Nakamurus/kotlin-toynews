package com.android.example.toynewsapplication.data.remote

import com.android.example.toynewsapplication.BuildConfig
import com.android.example.toynewsapplication.data.remote.api.GptApiService
import com.android.example.toynewsapplication.data.remote.api.NewsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


object ApiService {
    private const val BASE_URL_NEWS = "https://newsapi.org/v2/"
    private const val BASE_URL_GPT = "https://api.openai.com/v1/chat/"
    private const val apiKey = BuildConfig.OPENAI_API_KEY

    private val newsClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gptClient = OkHttpClient.Builder()
        .authenticator(GptAuthenticator(apiKey))
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    private val retrofitNews = Retrofit.Builder()
        .baseUrl(BASE_URL_NEWS)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(newsClient)
        .build()

    private val retrofitGpt = Retrofit.Builder()
        .baseUrl(BASE_URL_GPT)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(gptClient)
        .build()

    val newsApiService: NewsApiService by lazy {
        retrofitNews.create(NewsApiService::class.java)
    }

    val gptApiService: GptApiService by lazy {
        retrofitGpt.create(GptApiService::class.java)
    }
}

class GptAuthenticator(
    private val apiKey: String
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request().header("Authorization") != null) {
            return null
        }
        return response.request().newBuilder().header("Authorization", "Bearer $apiKey").build()
    }
}