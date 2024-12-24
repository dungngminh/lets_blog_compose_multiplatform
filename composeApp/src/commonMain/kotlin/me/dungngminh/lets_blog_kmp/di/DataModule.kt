package me.dungngminh.lets_blog_kmp.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.observable.makeObservable
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.dungngminh.lets_blog_kmp.data.api_service.AuthService
import me.dungngminh.lets_blog_kmp.data.api_service.createAuthService
import me.dungngminh.lets_blog_kmp.data.local.AppSettingStore
import me.dungngminh.lets_blog_kmp.data.local.UserStore
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
fun createHttpClient(block: HttpClientConfig<*>.() -> Unit = {}) =
    HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    prettyPrintIndent = "    "
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(Logging) {
            logger =
                object : Logger {
                    override fun log(message: String) {
                        Napier.v(message, null, "HTTP Client")
                    }
                }
            level = LogLevel.BODY
        }
        block()
    }

private val httpModule =
    module {
        single<HttpClient> {
            createHttpClient {
                install(Auth) {
                    bearer {
                        loadTokens {
                            val userStoreData = get<UserStore>().userStoreDataFlow.firstOrNull()
                            if (userStoreData != null) {
                                BearerTokens(
                                    accessToken = userStoreData.token,
                                    refreshToken = null,
                                )
                            } else {
                                null
                            }
                        }
                    }
                }
            }
        }

        single<Ktorfit> {
            Ktorfit
                .Builder()
                .httpClient(get<HttpClient>())
                .baseUrl("https://letsblog.up.railway.app/")
                .converterFactories(FlowConverterFactory())
                .build()
        }
    }

private val ApiModule =
    module {
        single<AuthService> {
            get<Ktorfit>()
                .createAuthService()
        }
    }

@OptIn(ExperimentalSettingsApi::class)
private val LocalModule =
    module {
        single<Settings> { Settings() }
        single<FlowSettings> {
            get<Settings>()
                .makeObservable()
                .toFlowSettings()
        }
        single<UserStore> { UserStore(get()) }
        single<AppSettingStore> { AppSettingStore(get()) }
    }

internal val DataModule =
    module {
        includes(LocalModule)
        includes(httpModule)
        includes(ApiModule)
    }
