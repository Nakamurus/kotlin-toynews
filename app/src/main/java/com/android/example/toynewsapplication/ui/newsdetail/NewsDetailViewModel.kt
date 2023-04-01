package com.android.example.toynewsapplication.ui.newsdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.toynewsapplication.data.domain.News

class NewsDetailViewModel(news: News): ViewModel() {

    private val _selectedNews = MutableLiveData<News>()
    val selectedNews: LiveData<News>
        get() = _selectedNews

    init {
        _selectedNews.value = news
    }
}