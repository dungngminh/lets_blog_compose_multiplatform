package me.dungngminh.lets_blog_kmp.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalSettingsApi::class)
class AppSettingStore(
    private val flowSettings: FlowSettings,
) {
    val isOnboardingCompletedFlow: Flow<Boolean> =
        flowSettings
            .getBooleanFlow(IS_ONBOARDING_COMPLETED_KEY, defaultValue = false)
            .distinctUntilChanged()

    suspend fun saveIsOnboardingCompleted(isCompleted: Boolean) {
        flowSettings.putBoolean(IS_ONBOARDING_COMPLETED_KEY, isCompleted)
    }

    companion object {
        private const val IS_ONBOARDING_COMPLETED_KEY = "letsblog_is_onboarding_completed"
    }
}
