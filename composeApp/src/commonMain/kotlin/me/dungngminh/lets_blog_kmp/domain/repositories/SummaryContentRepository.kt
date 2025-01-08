package me.dungngminh.lets_blog_kmp.domain.repositories

import kotlinx.coroutines.flow.Flow

interface SummaryContentRepository {
    suspend fun summaryContent(content: String): Result<String?>

    fun summaryContentFlow(content: String): Flow<String?>
}
