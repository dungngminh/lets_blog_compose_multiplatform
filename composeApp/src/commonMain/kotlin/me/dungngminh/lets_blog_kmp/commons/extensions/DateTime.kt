package me.dungngminh.lets_blog_kmp.commons.extensions

import androidx.compose.runtime.Composable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.time_ago_days
import letsblogkmp.composeapp.generated.resources.time_ago_hours
import letsblogkmp.composeapp.generated.resources.time_ago_just_now
import letsblogkmp.composeapp.generated.resources.time_ago_minutes
import letsblogkmp.composeapp.generated.resources.time_ago_months
import letsblogkmp.composeapp.generated.resources.time_ago_years
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

fun String.mapISODateTimeStringToLong(): Long =
    Instant
        .parse(this)
        .toEpochMilliseconds()

@Composable
fun Long.timeAgo(): String {
    val currentInstant = Clock.System.now()
    val currentLocalDateTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val currentYear = currentLocalDateTime.year
    val currentMonth = currentLocalDateTime.monthNumber
    val currentDay = currentLocalDateTime.dayOfMonth
    val currentHour = currentLocalDateTime.hour
    val currentMinute = currentLocalDateTime.minute

    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val year = localDateTime.year
    val month = localDateTime.monthNumber
    val day = localDateTime.dayOfMonth
    val hour = localDateTime.hour
    val minute = localDateTime.minute

    return when {
        currentYear - year > 0 ->
            pluralStringResource(
                Res.plurals.time_ago_years,
                currentYear - year,
                currentYear - year,
            )

        currentMonth - month > 0 ->
            pluralStringResource(
                Res.plurals.time_ago_months,
                currentMonth - month,
                currentMonth - month,
            )

        currentDay - day > 0 ->
            pluralStringResource(
                Res.plurals.time_ago_days,
                currentDay - day,
                currentDay - day,
            )

        currentHour - hour > 0 ->
            pluralStringResource(
                Res.plurals.time_ago_hours,
                currentHour - hour,
                currentHour - hour,
            )

        currentMinute - minute > 0 ->
            pluralStringResource(
                Res.plurals.time_ago_minutes,
                currentMinute - minute,
                currentMinute - minute,
            )

        else -> stringResource(Res.string.time_ago_just_now)
    }
}
