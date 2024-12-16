package me.dungngminh.lets_blog_kmp.commons.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AppTypography() =
    Typography(
        labelSmall =
            TextStyle(
                fontSize = 11.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
        labelMedium =
            TextStyle(
                fontSize = 12.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
        labelLarge =
            TextStyle(
                fontSize = 14.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
        bodySmall =
            TextStyle(
                fontSize = 12.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 14.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 16.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        headlineSmall =
            TextStyle(
                fontSize = 24.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
        headlineMedium =
            TextStyle(
                fontSize = 28.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
            ),
        headlineLarge =
            TextStyle(
                fontSize = 32.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
            ),
        displaySmall =
            TextStyle(
                fontSize = 36.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 44.sp,
                letterSpacing = 0.sp,
            ),
        displayMedium =
            TextStyle(
                fontSize = 45.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 52.sp,
                letterSpacing = 0.sp,
            ),
        displayLarge =
            TextStyle(
                fontSize = 57.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp,
            ),
        titleSmall =
            TextStyle(
                fontSize = 14.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
        titleMedium =
            TextStyle(
                fontSize = 16.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
            ),
        titleLarge =
            TextStyle(
                fontSize = 22.sp,
                fontFamily = NunitoFontFamily(),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
    )
