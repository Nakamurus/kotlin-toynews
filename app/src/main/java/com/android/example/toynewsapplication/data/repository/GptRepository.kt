package com.android.example.toynewsapplication.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import com.android.example.toynewsapplication.data.domain.News
import com.android.example.toynewsapplication.data.local.dao.GptDao
import com.android.example.toynewsapplication.data.local.dao.NewsDao
import com.android.example.toynewsapplication.data.local.model.GptEntity
import com.android.example.toynewsapplication.data.local.model.asDatabaseModel
import com.android.example.toynewsapplication.data.remote.api.GptApiService
import com.android.example.toynewsapplication.data.remote.model.GptResponseBody
import com.android.example.toynewsapplication.data.remote.model.createGptRequestBody
import com.android.example.toynewsapplication.data.remote.model.extractText
import com.android.example.toynewsapplication.util.exceptions.HttpExceptionError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GptRepository(
    private val gptApiService: GptApiService,
    private val gptDao: GptDao,
    private val newsDao: NewsDao
) {

    private suspend fun generateContext(news: News): Result<GptResponseBody, HttpExceptionError> = withContext(
        Dispatchers.IO) {
        Log.d("GptRepository", "news: $news")
        Log.d("GptRepository", "newsEntities: ${newsDao.getAllNews()}")
        repeat(3) { attempt ->
            Log.d("GptRepository", "Attempt $attempt")
            if (attempt > 0) {
                // Wait for 1 second before retrying
                Thread.sleep(1000)
            }
            val response = gptApiService.generateContext(news.createGptRequestBody())
            Log.d("GptRepository", "Response: $response")
            if (!response.isSuccessful && attempt == 2) {

                return@withContext Err(HttpExceptionError.fromStatusCode(response.code()))
            }
            val body = response.body() ?: return@withContext Err(HttpExceptionError.fromStatusCode(response.code()))
            Log.d("GptRepository", "Body: $body")
            return@withContext Ok(body)
        }
        return@withContext Err(HttpExceptionError.NetworkError)
    }

    suspend fun generateAndSaveContext(news: News): Result<String, Exception> = withContext(Dispatchers.IO) {
        val result = generateContext(news)
        return@withContext when (result) {
            is Ok -> {
                val newsId = news.id ?: newsDao.getNewsByTitle(news.title).id
                val context = result.value.extractText()
                val gpt = GptEntity(news_id = newsId, context = context)
                try {
                    gptDao.insertGpt(gpt)
                    Ok(context)
                } catch (e: SQLiteException) {
                    Log.e("GptRepository", "Error: ${e.message}")
                    Err(e)
                }
            }
            is Err -> {
                Log.e("GptRepository", "Error: ${result.error}")
                Err(Exception("Error generating context:  ${result.error}"))
            }
        }
    }
}