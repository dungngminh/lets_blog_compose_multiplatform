package me.dungngminh.lets_blog_kmp.commons.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T.toJsonStr(): String = Json.encodeToString(this)

inline fun <reified T> String.fromJsonStr(): T? =
    runCatching {
        Json.decodeFromString<T>(this)
    }.getOrNull()
