package me.dungngminh.lets_blog_kmp.di

import me.dungngminh.lets_blog_kmp.AppViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.sign_in.SignInViewModel
import me.dungngminh.lets_blog_kmp.presentation.sign_up.SignUpViewModel
import org.koin.dsl.module
import kotlin.jvm.JvmField

private val ViewModelModule =
    module {
        factory { SignInViewModel(get()) }
        factory { SignUpViewModel(get()) }
        factory { AppViewModel(get()) }
        factory { UserSessionViewModel(get()) }
    }

@JvmField
internal val AppModule =
    module {
        includes(DispatcherModule)
        includes(DataModule)
        includes(DomainModule)
        includes(ViewModelModule)
    }
