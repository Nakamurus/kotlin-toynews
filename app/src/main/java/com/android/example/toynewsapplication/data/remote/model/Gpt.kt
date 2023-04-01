package com.android.example.toynewsapplication.data.remote.model

data class GptResponseBody(
    val id: String,
    val gpt_object: String,
    val created: Long,
    val model: String,
    val choices: List<GptResponseChoice>
)

data class GptResponseChoice(
    val text: String,
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
    val model: String,
    val propmpt: String,
    val max_tokens: Int = 1024,
    val n: Int = 1,
    val temperature: Double = 0.5
)