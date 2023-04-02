package com.android.example.toynewsapplication.util.exceptions

import java.io.IOException

sealed class HttpExceptionError(message: String?): IOException(message) {
    object NetworkError : HttpExceptionError("Network error")
    object EmptyResponseError: HttpExceptionError("Empty response error")
    private object BadRequest : HttpExceptionError("Bad request")
    private object Unauthorized : HttpExceptionError("Unauthorized")
    private object Forbidden : HttpExceptionError("Forbidden")
    private object NotFound : HttpExceptionError("Not found")
    private object TooManyRequests : HttpExceptionError("Too many requests")
    private object ServerError : HttpExceptionError("Server error")
    private object UnknownError : HttpExceptionError("Unknown error")

    companion object {
        fun fromStatusCode(statusCode: Int?): HttpExceptionError {
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