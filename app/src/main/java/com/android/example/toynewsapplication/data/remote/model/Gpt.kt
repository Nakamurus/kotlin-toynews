package com.android.example.toynewsapplication.data.remote.model

import com.android.example.toynewsapplication.data.domain.News

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
    val model: String = "gpt-3.5-turbo",
    val prompt: String,
    val max_tokens: Int = 1024,
    val n: Int = 1,
    val temperature: Double = 0.5
)

fun GptResponseBody.extractText(): String {
    return choices[0].text
}

fun News.createGptRequestBody(): GptRequestBody {
    return GptRequestBody(
        prompt = "" +
                "This is a news article about ${title}. $description $content" +
                "Return informational context which is necessary to understand the article" +
                "based on the scientific research on this topic." +
                "Response structure:" +
                "1. Summary of the context" +
                "2. Main points of the context" +
                "3. Further study relevant to the context" +
                "Please write an answer in Japanese"
    )
}