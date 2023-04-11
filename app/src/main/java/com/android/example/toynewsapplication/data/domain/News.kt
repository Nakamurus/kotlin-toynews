package com.android.example.toynewsapplication.data.domain

import android.os.Parcelable
import com.android.example.toynewsapplication.data.local.model.GptEntity
import com.android.example.toynewsapplication.data.local.model.NewsEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val id: Int? = null,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String?,
    val context: String?
): Parcelable
