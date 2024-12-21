package me.dungngminh.lets_blog_kmp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.base(DebugAntilog())
        // Navigation icon color can be changed since API 26(O)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            enableEdgeToEdge()
        } else {
            enableEdgeToEdge(
                statusBarStyle =
                    SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                    ),
                navigationBarStyle =
                    SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                    ),
            )

            // For API29(Q) or higher and 3-button navigation,
            // the following code must be written to make the navigation color completely transparent.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
