package com.android.example.toynewsapplication.data.domain

import android.os.Parcelable
import com.android.example.toynewsapplication.data.local.model.GptEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String?,
    val context: String?
): Parcelable

fun String.asDatabaseModel(newsId: Int): GptEntity {
    return GptEntity(
        newsId = newsId,
        context = this
    )
}