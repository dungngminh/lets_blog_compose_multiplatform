package me.dungngminh.lets_blog_kmp.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val ioDispatcher: CoroutineDispatcher
    get() = Dispatchers.IO
