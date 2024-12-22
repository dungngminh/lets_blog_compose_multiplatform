package me.dungngminh.lets_blog_kmp.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class UserStore(
    private val flowSettings: FlowSettings,
) {
    val tokenFlow: Flow<String?> = flowSettings.getStringOrNullFlow(TOKEN_KEY)

    suspend fun saveToken(token: String) {
        flowSettings.putString(TOKEN_KEY, token)
    }

    companion object {
        private const val TOKEN_KEY = "letsblog_token"
    }
}
