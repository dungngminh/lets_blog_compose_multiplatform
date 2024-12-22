package me.dungngminh.lets_blog_kmp.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val ioDispatcher: CoroutineDispatcher
    get() = Dispatchers.IO
