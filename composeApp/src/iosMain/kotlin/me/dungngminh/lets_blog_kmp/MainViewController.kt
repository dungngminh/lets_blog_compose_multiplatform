package me.dungngminh.lets_blog_kmp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIViewController

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainViewController(): UIViewController {
    Napier.base(DebugAntilog())
    return ComposeUIViewController {
        val windowSizeClass = calculateWindowSizeClass()
        App(windowSizeClass = windowSizeClass)
    }
}
