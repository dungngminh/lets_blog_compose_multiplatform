package me.dungngminh.lets_blog_kmp.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.observable.makeObservable
import de.jensklingenberg.ktorfit.Ktorfit
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.dungngminh.lets_blog_kmp.data.api_service.AuthService
import me.dungngminh.lets_blog_kmp.data.api_service.BlogService
import me.dungngminh.lets_blog_kmp.data.api_service.FavoriteService
import me.dungngminh.lets_blog_kmp.data.api_service.UploadDocumentService
import me.dungngminh.lets_blog_kmp.data.api_service.UserService
import me.dungngminh.lets_blog_kmp.data.api_service.createAuthService
import me.dungngminh.lets_blog_kmp.data.api_service.createBlogService
import me.dungngminh.lets_blog_kmp.data.api_service.createFavoriteService
import me.dungngminh.lets_blog_kmp.data.api_service.createUploadDocumentService
import me.dungngminh.lets_blog_kmp.data.api_service.createUserService
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
            level = LogLevel.ALL
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
                            val userStoreData = get<UserStore>().getUserStoreData()
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
//                .baseUrl("http://192.168.1.113:8080/")
                .baseUrl("https://letsblog.up.railway.app/")
                .build()
        }
    }

private val ApiModule =
    module {
        single<AuthService> {
            get<Ktorfit>()
                .createAuthService()
        }
        single<UserService> {
            get<Ktorfit>()
                .createUserService()
        }
        single<BlogService> {
            get<Ktorfit>()
                .createBlogService()
        }
        single<UploadDocumentService> {
            get<Ktorfit>()
                .createUploadDocumentService()
        }
        single<FavoriteService> {
            get<Ktorfit>()
                .createFavoriteService()
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

private val GenerativeAIModule =
    module {
        single {
            GenerativeModel(
                modelName = "gemini-1.5-flash-8b",
                apiKey = "PASTE-YOUR-GEMINI-KEY-HERE",
            )
        }
    }

internal val DataModule =
    module {
        includes(LocalModule)
        includes(httpModule)
        includes(ApiModule)
        includes(GenerativeAIModule)
    }
