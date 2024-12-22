package me.dungngminh.lets_blog_kmp.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.dungngminh.lets_blog_kmp.data.datasource.RemoteAuthDatasource
import me.dungngminh.lets_blog_kmp.data.repositories.AuthRepositoryImpl
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import org.koin.dsl.module

private val KtorfitInstance =
    module {
        single {
            Ktorfit
                .Builder()
                .baseUrl("https://backend.backend.orb.local/")
                .build()
        }
    }

internal val DataModule =
    module {
        includes(KtorfitInstance)
        single { RemoteAuthDatasource(get()) }
        single<AuthRepository> { AuthRepositoryImpl(get()) }
    }
