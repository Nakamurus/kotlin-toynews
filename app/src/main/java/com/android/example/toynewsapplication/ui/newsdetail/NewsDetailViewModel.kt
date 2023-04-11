package com.android.example.toynewsapplication.ui.newsdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.toynewsapplication.data.domain.News
import com.android.example.toynewsapplication.data.local.model.extractContext
import com.android.example.toynewsapplication.data.repository.GptRepository
import com.android.example.toynewsapplication.data.repository.NewsRepository
import com.android.example.toynewsapplication.util.ApiState
import com.android.example.toynewsapplication.util.exceptions.HttpExceptionError
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getOr
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch

class NewsDetailViewModel(
    private val gptRepository: GptRepository,
    news: News): ViewModel() {

    private val _state =  MutableLiveData<ApiState>(ApiState.Loading)
    val state:LiveData<ApiState>
        get() = _state

    private val _selectedNews = MutableLiveData<News>(news)
    val selectedNews: LiveData<News>
        get() = _selectedNews

    private val _context = MutableLiveData<String>("")
    val context: LiveData<String>
        get() = _context

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() = viewModelScope.launch {
        _state.value = ApiState.Loading
        Log.d("NewsListViewModel", "Loading...")
        Log.d("NewsListViewModel", "${state.value}")

        gptRepository.generateAndSaveContext(selectedNews.value!!)
            .onSuccess {
                Log.d("NewsListViewModel", "Success! $it")
                _state.value = ApiState.Success
                _context.value = it
                Log.d("NewsListViewModel", "${state.value}")
            }
            .onFailure {
                Log.d("NewsListViewModel", "Failure! $it")
                _state.value = ApiState.Failure(it)
            }
    }
}