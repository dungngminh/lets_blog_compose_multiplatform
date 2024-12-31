package me.dungngminh.lets_blog_kmp.domain.entities

import kotlinx.serialization.Serializable
import letsblogkmp.composeapp.generated.resources.Res
import letsblogkmp.composeapp.generated.resources.blog_category_business
import letsblogkmp.composeapp.generated.resources.blog_category_education
import letsblogkmp.composeapp.generated.resources.blog_category_fashion
import letsblogkmp.composeapp.generated.resources.blog_category_food
import letsblogkmp.composeapp.generated.resources.blog_category_technology
import letsblogkmp.composeapp.generated.resources.blog_category_travel
import org.jetbrains.compose.resources.StringResource
import kotlin.jvm.JvmStatic

@Serializable
enum class BlogCategory {
    BUSINESS,
    TECHNOLOGY,
    FASHION,
    TRAVEL,
    FOOD,
    EDUCATION,
    ;

    fun localizedStringRes(): StringResource =
        when (this) {
            BUSINESS -> Res.string.blog_category_business
            TECHNOLOGY -> Res.string.blog_category_technology
            FASHION -> Res.string.blog_category_fashion
            TRAVEL -> Res.string.blog_category_travel
            FOOD -> Res.string.blog_category_food
            EDUCATION -> Res.string.blog_category_education
        }

    companion object {
        @JvmStatic
        fun fromString(value: String): BlogCategory = valueOf(value.uppercase())
    }
}
