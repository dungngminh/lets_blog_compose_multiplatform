package me.dungngminh.lets_blog_kmp.di

import me.dungngminh.lets_blog_kmp.presentation.sign_in.SignInViewModel
import me.dungngminh.lets_blog_kmp.presentation.sign_up.SignUpViewModel
import org.koin.dsl.module
import kotlin.jvm.JvmField

private val SignInModule =
    module {
        factory { SignInViewModel(get()) }
    }

private val SignUpModule =
    module {
        factory { SignUpViewModel(get()) }
    }

private val ViewModelModule =
    module {
        includes(SignInModule)
        includes(SignUpModule)
    }

@JvmField
internal val AppModule =
    module {
        includes(DispatcherModule)
        includes(DataModule)
        includes(DomainModule)
        includes(ViewModelModule)
    }
