package me.dungngminh.lets_blog_kmp.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val ioDispatcher: CoroutineDispatcher

internal val DispatcherModule =
    module {
        single(named("Dispatchers.Main")) { Dispatchers.Main }
        single(named("Dispatchers.Default")) { Dispatchers.Default }
    }
