package me.dungngminh.lets_blog_kmp.presentation.edit_blog

import androidx.compose.ui.text.TextRange
import cafe.adriel.voyager.core.model.ScreenModel
import com.mohamedrejeb.richeditor.model.RichTextState
import me.dungngminh.lets_blog_kmp.domain.entities.Blog

class EditBlogViewModel(
    private val blog: Blog,
) : ScreenModel {
    val richTextState =
        RichTextState().apply {
            setHtml(blog.content)
            selection = TextRange(0)
        }
}
