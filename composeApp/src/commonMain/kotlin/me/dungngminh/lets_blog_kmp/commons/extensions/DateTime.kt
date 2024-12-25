package me.dungngminh.lets_blog_kmp.commons.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun String.mapISODateTimeStringToLong(): Long =
    LocalDateTime.Formats.ISO
        .parse(this)
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()
