package com.android.example.toynewsapplication.util

import com.android.example.toynewsapplication.util.exceptions.HttpExceptionError


sealed class ApiState {
    object Loading: ApiState()
    object Success: ApiState()
    data class Failure(val error: Exception): ApiState()
}
