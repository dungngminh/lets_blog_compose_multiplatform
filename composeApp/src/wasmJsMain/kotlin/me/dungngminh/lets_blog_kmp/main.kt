package me.dungngminh.lets_blog_kmp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
fun main() {
    Napier.base(DebugAntilog())
    ComposeViewport(document.body!!) {
        val windowSizeClass = calculateWindowSizeClass()
        App(windowSizeClass = windowSizeClass)
    }
}
