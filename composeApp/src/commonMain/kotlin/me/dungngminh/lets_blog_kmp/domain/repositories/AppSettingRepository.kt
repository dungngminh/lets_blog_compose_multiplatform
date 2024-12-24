package me.dungngminh.lets_blog_kmp.domain.repositories

import kotlinx.coroutines.flow.Flow

interface AppSettingRepository {
    val isOnboardingCompletedFlow: Flow<Boolean>

    suspend fun saveIsOnboardingCompleted(isCompleted: Boolean)
}
