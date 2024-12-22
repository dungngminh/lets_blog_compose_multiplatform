package me.dungngminh.lets_blog_kmp.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.observable.makeObservable
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.dungngminh.lets_blog_kmp.data.api_service.createAuthService
import me.dungngminh.lets_blog_kmp.data.local.UserStore
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
private val httpModule =
    module {
        single {
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
            }
        }

        single {
            Ktorfit
                .Builder()
                .httpClient(get<HttpClient>())
                .baseUrl("http://192.168.1.112:1311/")
                .converterFactories(FlowConverterFactory())
                .build()
        }
    }

private val ApiModule =
    module {
        single { get<Ktorfit>().createAuthService() }
    }

@OptIn(ExperimentalSettingsApi::class)
private val LocalModule =
    module {
        single { Settings() }
        single {
            UserStore(
                get<Settings>().makeObservable().toFlowSettings(),
            )
        }
    }

internal val DataModule =
    module {
        includes(httpModule)
        includes(ApiModule)
        includes(LocalModule)
    }
