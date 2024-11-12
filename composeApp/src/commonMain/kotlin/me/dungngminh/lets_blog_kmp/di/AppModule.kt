package me.dungngminh.lets_blog_kmp.di

import org.koin.dsl.module
import kotlin.jvm.JvmField

@JvmField
internal val AppModule =
    module {
        includes(DataModule)
    }
