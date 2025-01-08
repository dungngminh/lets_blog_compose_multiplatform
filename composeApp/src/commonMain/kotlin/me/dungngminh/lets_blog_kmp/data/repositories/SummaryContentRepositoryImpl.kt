package me.dungngminh.lets_blog_kmp.data.repositories

import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.Content
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.dungngminh.lets_blog_kmp.domain.repositories.SummaryContentRepository

class SummaryContentRepositoryImpl(
    private val generativeModel: GenerativeModel,
    private val ioDispatcher: CoroutineDispatcher,
) : SummaryContentRepository {
    override suspend fun summaryContent(content: String): Result<String?> =
        withContext(ioDispatcher) {
            runCatching {
                val prompt = buildPrompt(content)
                val response = generativeModel.generateContent(prompt)
                response.text
            }
        }

    override fun summaryContentFlow(content: String): Flow<String> {
        val prompt = buildPrompt(content)
        return generativeModel
            .generateContentStream(prompt)
            .map { it.text.orEmpty() }
            .flowOn(ioDispatcher)
    }

    private fun buildPrompt(content: String): Content =
        content {
            text(
                "I am a blog reader. " +
                    "I want to understand the " +
                    "main ideas of this blog post and " +
                    "how it should be summarized in the following points: " +
                    "Introduction, " +
                    "how many parts the content section has, " +
                    "a short summary of each part and a conclusion that summarizes the whole content. " +
                    "Please return it in markdown format. " +
                    "Here is the content of the entire post:",
            )
            text(content)
        }
}
