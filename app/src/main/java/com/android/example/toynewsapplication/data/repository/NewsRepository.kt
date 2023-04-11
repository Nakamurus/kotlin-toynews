package com.android.example.toynewsapplication.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Err


import com.android.example.toynewsapplication.BuildConfig
import com.android.example.toynewsapplication.data.domain.News
import com.android.example.toynewsapplication.data.local.dao.NewsDao
import com.android.example.toynewsapplication.data.local.model.GptEntity
import com.android.example.toynewsapplication.data.local.model.NewsEntity
import com.android.example.toynewsapplication.data.local.model.asDatabaseModel
import com.android.example.toynewsapplication.data.remote.model.asDomainModel
import com.android.example.toynewsapplication.data.local.model.asDomainModel
import com.android.example.toynewsapplication.data.remote.ApiService
import com.android.example.toynewsapplication.data.remote.api.NewsApiService
import com.android.example.toynewsapplication.data.remote.model.GptRequestBody
import com.android.example.toynewsapplication.util.exceptions.HttpExceptionError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class NewsRepository(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao) {

    private suspend fun fetchTopHeadlines(country: String): Result<List<News>, HttpExceptionError> =
        withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.NEWS_API_KEY

            repeat(3) { attempt ->
                if (attempt > 0) {
                    // Wait for 1 second before retrying
                    Thread.sleep(1000)
                }
                val response = newsApiService.getTopHeadlines(country, apiKey)
                if (!response.isSuccessful && attempt == 2) {
                    return@withContext Err(HttpExceptionError.fromStatusCode(response.code()))
                }
                val body = response.body() ?: return@withContext Err(HttpExceptionError.fromStatusCode(response.code()))

                val articles = body.articles?.asDomainModel() ?: return@withContext Err(HttpExceptionError.EmptyResponseError)

                return@withContext Ok(articles)
            }
            return@withContext Err(HttpExceptionError.NetworkError)
        }
    suspend fun fetchAndSaveTopHeadlines(country: String): Result<List<News>, Exception> {
        return when (val result = fetchTopHeadlines(country)) {
            is Ok -> {
                try {
                    val articles = result.value
                    val insertedIds = newsDao.insertAll(articles.asDatabaseModel())
                    Log.d("NewsRepository", "Inserted ids: $insertedIds")
                    Log.d("NewsRepository", "Inserted articles: $articles")
                    val articlesWithId = articles.mapIndexed { index, news ->
                        news.copy(id = insertedIds[index].toInt())
                    }
                    Log.d("NewsRepository", "Inserted articles with ids: $articlesWithId")
                    Ok(articlesWithId)
                } catch (e: SQLiteException) {
                    Err(e)
                }
            }
            is Err -> {
                Err(result.error)
            }
        }
    }
}

