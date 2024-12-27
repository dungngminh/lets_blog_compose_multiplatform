package me.dungngminh.lets_blog_kmp.commons.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.datetime.toInstant

fun String.mapISODateTimeStringToLong(): Long =
    Instant.parse(this)
        .toEpochMilliseconds()
