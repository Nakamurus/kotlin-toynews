package com.android.example.toynewsapplication.ui.newslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.toynewsapplication.data.domain.News
import com.android.example.toynewsapplication.data.local.model.asDomainModel
import com.android.example.toynewsapplication.data.repository.NewsRepository
import com.android.example.toynewsapplication.util.ApiState
import com.android.example.toynewsapplication.util.exceptions.HttpExceptionError
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch


class NewsListViewModel(
    private val newsRepository: NewsRepository,
): ViewModel() {

    private val _state =  MutableLiveData<ApiState>(ApiState.Loading)
    val state:LiveData<ApiState>
        get() = _state

    private val _navigateToSelectedNews = MutableLiveData<News?>()
    val navigateToSelectedNews: LiveData<News?>
        get() = _navigateToSelectedNews

    fun displayNewsDetails(news: News) {
        _navigateToSelectedNews.value = news
    }

    fun displayNewsDetailsComplete() {
        _navigateToSelectedNews.value = null
    }

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>>
        get() = _news

    init {
        refreshDataFromRepository("us")
    }

    private fun refreshDataFromRepository(country: String) = viewModelScope.launch {
        _state.value = ApiState.Loading
        Log.d("NewsListViewModel", "Loading...")
        Log.d("NewsListViewModel", "${state.value}")

        newsRepository.fetchAndSaveTopHeadlines(country)
            .onSuccess {
                Log.d("NewsListViewModel", "Success! $it")
                _state.value = ApiState.Success

                _news.value = it
                Log.d("NewsListViewModel", "${state.value}")
            }
            .onFailure {
                Log.d("NewsListViewModel", "Failure! $it")
                _state.value = ApiState.Failure(it as HttpExceptionError)
            }
    }
}
