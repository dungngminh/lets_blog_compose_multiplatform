package me.dungngminh.lets_blog_kmp

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun MainVC() {
    Napier.base(DebugAntilog())
    ComposeUIViewController { App() }
}
