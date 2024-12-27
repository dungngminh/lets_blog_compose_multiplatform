package me.dungngminh.lets_blog_kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.awt.Dimension

fun main() {
    Napier.base(DebugAntilog())
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "LetsBlogKMP",
        ) {
            window.minimumSize = Dimension(500, 800)
            App()
        }
    }
}
