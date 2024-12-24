package me.dungngminh.lets_blog_kmp.di

import me.dungngminh.lets_blog_kmp.data.repositories.AppSettingRepositoryImpl
import me.dungngminh.lets_blog_kmp.data.repositories.AuthRepositoryImpl
import me.dungngminh.lets_blog_kmp.domain.repositories.AppSettingRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import org.koin.dsl.module

internal val DomainModule =
    module {
        single<AuthRepository> {
            AuthRepositoryImpl(
                authService = get(),
                userStore = get(),
                ioDispatcher = ioDispatcher,
            )
        }
        single<AppSettingRepository> {
            AppSettingRepositoryImpl(
                appSettingStore = get(),
            )
        }
    }
