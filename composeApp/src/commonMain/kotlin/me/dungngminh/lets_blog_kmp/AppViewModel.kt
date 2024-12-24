package me.dungngminh.lets_blog_kmp

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.dungngminh.lets_blog_kmp.domain.repositories.AppSettingRepository

data class AppState(
    val isOnboardingCompleted: Boolean = false,
)

class AppViewModel(
    private val appSettingRepository: AppSettingRepository,
) : ScreenModel {
    val state =
        appSettingRepository
            .isOnboardingCompletedFlow
            .map {
                AppState(isOnboardingCompleted = it)
            }.stateIn(
                scope = screenModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AppState(),
            )

    fun saveIsOnboardingCompleted() {
        screenModelScope.launch {
            appSettingRepository.saveIsOnboardingCompleted(true)
        }
    }
}
