package com.android.example.toynewsapplication.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.example.toynewsapplication.data.domain.News

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val author: String?,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String?
)

fun List<NewsEntity>.asDomainModel(): List<News> {
    return map {
        News(
            title = it.title,
            description = it.description,
            author = it.author,
            url = it.url,
            urlToImage = it.imageUrl,
            publishedAt = it.publishedAt,
            content = it.content
        )
    }
}

fun List<News>.asDatabaseModel(): List<NewsEntity> {
    return map {
        NewsEntity(
            title = it.title,
            author = it.author,
            description = it.description,
            url = it.url,
            imageUrl = it.urlToImage,
            publishedAt = it.publishedAt,
            content = it.content
        )
    }
}