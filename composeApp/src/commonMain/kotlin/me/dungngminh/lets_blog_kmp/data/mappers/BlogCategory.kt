package me.dungngminh.lets_blog_kmp.data.mappers

import me.dungngminh.lets_blog_kmp.domain.entities.BlogCategory

fun BlogCategory.toApiString() =
    when (this) {
        BlogCategory.BUSINESS -> "business"
        BlogCategory.TECHNOLOGY -> "technology"
        BlogCategory.FASHION -> "fashion"
        BlogCategory.TRAVEL -> "travel"
        BlogCategory.FOOD -> "food"
        BlogCategory.EDUCATION -> "education"
    }
