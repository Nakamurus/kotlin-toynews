package com.android.example.toynewsapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Err


import com.android.example.toynewsapplication.BuildConfig
import com.android.example.toynewsapplication.data.domain.News
import com.android.example.toynewsapplication.data.local.dao.NewsDao
import com.android.example.toynewsapplication.data.local.model.asDatabaseModel
import com.android.example.toynewsapplication.data.remote.model.asDomainModel
import com.android.example.toynewsapplication.data.local.model.asDomainModel
import com.android.example.toynewsapplication.data.remote.api.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class NewsRepository(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao) {

    val topHeadlines: LiveData<List<News>> = Transformations.map(
        newsDao.getAllNews()
    ) {
        println(it)
        it.asDomainModel() }

    suspend fun getTopHeadlines(country: String): Result<Unit, NewsApiError>
    = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.NEWS_API_KEY

        repeat(3) { attempt ->
            if (attempt > 0) {
                // Wait for 1 second before retrying
                Thread.sleep(1000)
            }
            val response = newsApiService.getTopHeadlines(country, apiKey)
            if (!response.isSuccessful && attempt == 2) {
                return@withContext Err(NewsApiError.fromStatusCode(response.code()))
            }
            val body = response.body() ?: return@withContext Err(NewsApiError.fromStatusCode(response.code()))

            val articles = body.articles ?: return@withContext Err(NewsApiError.EmptyResponseError)

            newsDao.insertAll(articles.asDomainModel().asDatabaseModel())

            return@withContext Ok(Unit)
        }
        return@withContext Err(NewsApiError.NetworkError)
    }
}


sealed class NewsApiError(private val message: String?) {
    object NetworkError : NewsApiError("Network error")
    object EmptyResponseError: NewsApiError("Empty response error")
    private object BadRequest : NewsApiError("Bad request")
    private object Unauthorized : NewsApiError("Unauthorized")
    private object Forbidden : NewsApiError("Forbidden")
    private object NotFound : NewsApiError("Not found")
    private object TooManyRequests : NewsApiError("Too many requests")
    private object ServerError : NewsApiError("Server error")
    private object UnknownError : NewsApiError("Unknown error")

    companion object {
        fun fromStatusCode(statusCode: Int?): NewsApiError {
            return when (statusCode) {
                400 -> BadRequest
                401 -> Unauthorized
                403 -> Forbidden
                404 -> NotFound
                429 -> TooManyRequests
                in 500..599 -> ServerError
                else -> UnknownError
            }
        }
    }

    override fun toString(): String {
        return message ?: super.toString()
    }
}
