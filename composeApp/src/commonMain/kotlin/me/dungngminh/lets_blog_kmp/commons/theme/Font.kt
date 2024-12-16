package me.dungngminh.lets_blog_kmp.commons.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.nunito_black
import letsblogkmp.composeapp.generated.resources.nunito_bold
import letsblogkmp.composeapp.generated.resources.nunito_extra_bold
import letsblogkmp.composeapp.generated.resources.nunito_extra_light
import letsblogkmp.composeapp.generated.resources.nunito_italic
import letsblogkmp.composeapp.generated.resources.nunito_light
import letsblogkmp.composeapp.generated.resources.nunito_medium
import letsblogkmp.composeapp.generated.resources.nunito_regular
import letsblogkmp.composeapp.generated.resources.nunito_semi_bold
import org.jetbrains.compose.resources.Font

@Composable
fun NunitoFontFamily() =
    FontFamily(
        Font(Res.font.nunito_italic, style = FontStyle.Italic),
        Font(Res.font.nunito_extra_light, weight = FontWeight.ExtraLight),
        Font(Res.font.nunito_light, weight = FontWeight.Light),
        Font(Res.font.nunito_regular, weight = FontWeight.Normal),
        Font(Res.font.nunito_medium, weight = FontWeight.Medium),
        Font(Res.font.nunito_semi_bold, weight = FontWeight.SemiBold),
        Font(Res.font.nunito_bold, weight = FontWeight.Bold),
        Font(Res.font.nunito_extra_bold, weight = FontWeight.ExtraBold),
        Font(Res.font.nunito_black, weight = FontWeight.Black),
    )
