package me.dungngminh.lets_blog_kmp.di

import me.dungngminh.lets_blog_kmp.data.repositories.AppSettingRepositoryImpl
import me.dungngminh.lets_blog_kmp.data.repositories.AuthRepositoryImpl
import me.dungngminh.lets_blog_kmp.data.repositories.BlogRepositoryImpl
import me.dungngminh.lets_blog_kmp.data.repositories.UploadDocumentRepositoryImpl
import me.dungngminh.lets_blog_kmp.data.repositories.UserRepositoryImpl
import me.dungngminh.lets_blog_kmp.domain.repositories.AppSettingRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.AuthRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.BlogRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UploadDocumentRepository
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository
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
        single<BlogRepository> {
            BlogRepositoryImpl(
                blogService = get(),
                ioDispatcher = ioDispatcher,
            )
        }
        single<UserRepository> {
            UserRepositoryImpl(
                userService = get(),
                ioDispatcher = ioDispatcher,
            )
        }
        single<UploadDocumentRepository> {
            UploadDocumentRepositoryImpl(
                uploadDocumentService = get(),
                ioDispatcher = ioDispatcher,
            )
        }
    }
