package me.dungngminh.lets_blog_kmp.presentation.detail_blog

import cafe.adriel.voyager.core.model.ScreenModel
import io.github.aakira.napier.Napier
import me.dungngminh.lets_blog_kmp.domain.entities.Blog

class DetailBlogViewModel(
    private val blog: Blog,
) : ScreenModel {
    init {
        Napier.d("DetailBlogViewModel: $blog")
    }
}
