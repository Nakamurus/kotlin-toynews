package com.android.example.toynewsapplication.ui.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.toynewsapplication.data.domain.News

class NewsDetailViewModelFactory(private val news: News): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsDetailViewModel::class.java)) {
            return NewsDetailViewModel(news) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}