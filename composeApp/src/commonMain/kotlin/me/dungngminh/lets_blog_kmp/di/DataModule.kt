package me.dungngminh.lets_blog_kmp.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.dungngminh.lets_blog_kmp.data.datasource.RemoteAuthDatasource
import me.dungngminh.lets_blog_kmp.data.repositories.AuthRepositoryImpl
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import org.koin.dsl.module

private val KtorHttpClientModule =
    module {
        single {
            HttpClient {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Any,
                    )
                }
            }
        }
    }

internal val DataModule =
    module {
        includes(KtorHttpClientModule)
        single { RemoteAuthDatasource(get()) }
        single<AuthRepository> { AuthRepositoryImpl(get()) }
    }
