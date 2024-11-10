package me.dungngminh.lets_blog_kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "LetsBlogKMP",
        ) {
            App()
        }
    }
