package me.dungngminh.lets_blog_kmp.data.repositories

import kotlinx.coroutines.flow.Flow
import me.dungngminh.lets_blog_kmp.data.local.AppSettingStore
import me.dungngminh.lets_blog_kmp.domain.repositories.AppSettingRepository

class AppSettingRepositoryImpl(
    private val appSettingStore: AppSettingStore,
) : AppSettingRepository {
    override val isOnboardingCompletedFlow: Flow<Boolean>
        get() = appSettingStore.isOnboardingCompletedFlow

    override suspend fun saveIsOnboardingCompleted(isCompleted: Boolean) {
        appSettingStore.saveIsOnboardingCompleted(isCompleted)
    }
}
