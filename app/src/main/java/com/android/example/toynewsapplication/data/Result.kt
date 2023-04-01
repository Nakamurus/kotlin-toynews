package com.android.example.toynewsapplication.data

sealed class NewsApiError(val message: String?) {
    object NetworkError : NewsApiError("Network error")
    object BadRequest : NewsApiError("Bad request")
    object Unauthorized : NewsApiError("Unauthorized")
    object Forbidden : NewsApiError("Forbidden")
    object NotFound : NewsApiError("Not found")
    object TooManyRequests : NewsApiError("Too many requests")
    object ServerError : NewsApiError("Server error")
    object UnknownError : NewsApiError("Unknown error")

    companion object {
        fun fromStatusCode(statusCode: Int?): NewsApiError {
            return when (statusCode) {
                400 -> BadRequest
                401 -> Unauthorized
                403 -> Forbidden
                404 -> NotFound
                429 -> TooManyRequests
                in 500..599 -> ServerError
                else -> UnknownError
            }
        }
    }
}
