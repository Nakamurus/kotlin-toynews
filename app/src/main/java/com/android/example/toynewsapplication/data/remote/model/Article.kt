package com.android.example.toynewsapplication.data.remote.model

import com.android.example.toynewsapplication.data.domain.News


data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class Source(
    val id: String?,
    val name: String
)

fun List<Article>.asDomainModel(): List<News> {
    return map {
        News(
            author = it.author,
            title = it.title,
            description = it.description ?: "",
            url = it.url,
            urlToImage = it.urlToImage ?: "",
            publishedAt = it.publishedAt,
            content = it.content
        )
    }
}