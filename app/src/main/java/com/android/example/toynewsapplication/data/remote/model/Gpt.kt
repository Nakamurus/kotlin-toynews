package com.android.example.toynewsapplication.data.remote.model

import com.android.example.toynewsapplication.data.domain.News
import com.squareup.moshi.Json

data class GptResponseBody(
    val id: String,
    @Json(name = "object")
    val gpt_aobject: String,
    val created: Long,
    val model: String,
    val usage: Usage,
    val choices: List<GptResponseChoice>
)

data class GptResponseChoice(
    val message: Message,
    val index: Int,
    val logprobs: GptLogprobs?,
    val finish_reason: String
)


data class GptLogprobs(
    val token_logprobs: List<List<Double>>,
    val top_logprobs: List<Double>,
    val text_offset: List<Int>?
)

data class GptRequestBody(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    val max_tokens: Int = 1024,
    val n: Int = 1,
    val temperature: Double = 0.5
)

data class Message(
    val role: String,
    val content: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

fun GptResponseBody.extractText(): String {
    return choices[0].message.content
}

fun News.createGptRequestBody(): GptRequestBody {
    return GptRequestBody(
        messages = listOf<Message>(
            Message(
                "system",
                "You are a specialist in the field of $title$description, $content. You have an order which is to write background article based on the scientific research on this topic." +
                        "Response structure:" +
                        "1. Summary of the context" +
                        "2. Main points of the context" +
                        "3. Further study relevant to the context"
            )
        )
    )
}