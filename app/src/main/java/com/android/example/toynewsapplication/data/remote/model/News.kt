package com.android.example.toynewsapplication.data.remote.model

data class NewsResponse(
    val status: String,
    val totalResults: Int?,
    val articles: List<Article>?
)