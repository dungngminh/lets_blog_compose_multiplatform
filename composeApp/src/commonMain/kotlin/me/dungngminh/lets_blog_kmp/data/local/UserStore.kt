package me.dungngminh.lets_blog_kmp.data.local

import com.hoc081098.flowext.FlowExtPreview
import com.hoc081098.flowext.catchAndReturn
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class UserStoreData(
    val token: String,
    val userId: String,
)

@OptIn(ExperimentalSettingsApi::class)
class UserStore(
    private val flowSettings: FlowSettings,
) {
    @OptIn(FlowExtPreview::class)
    val userStoreDataFlow: Flow<UserStoreData?> =
        flowSettings
            .getStringOrNullFlow(TOKEN_KEY)
            .map { it?.let { Json.decodeFromString<UserStoreData>(it) } }
            .catchAndReturn(null)
            .distinctUntilChanged()

    suspend fun updateUserData(userStoreData: UserStoreData) {
        flowSettings.putString(TOKEN_KEY, Json.encodeToString(userStoreData))
    }

    suspend fun clearUserData() {
        flowSettings.remove(TOKEN_KEY)
    }

    companion object {
        private const val TOKEN_KEY = "letsblog_token"
    }
}
