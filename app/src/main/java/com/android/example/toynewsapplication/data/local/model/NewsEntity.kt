package com.android.example.toynewsapplication.data.local.model

import androidx.room.ColumnInfo
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
            id = it.id,
            title = it.title,
            description = it.description,
            author = it.author,
            url = it.url,
            urlToImage = it.imageUrl,
            publishedAt = it.publishedAt,
            content = it.content,
            context = ""
        )
    }
}

fun List<News>.populateId(idList: List<Int>): List<News> {
    return mapIndexed { index, news ->
        News(
            id = idList[index],
            title = news.title,
            description = news.description,
            author = news.author,
            url = news.url,
            urlToImage = news.urlToImage,
            publishedAt = news.publishedAt,
            content = news.content,
            context = ""
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

fun News.asDatabaseModel(): NewsEntity {
    return NewsEntity(
        id = this.id ?: 0,
        title = this.title,
        author = this.author,
        description = this.description,
        url = this.url,
        imageUrl = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}