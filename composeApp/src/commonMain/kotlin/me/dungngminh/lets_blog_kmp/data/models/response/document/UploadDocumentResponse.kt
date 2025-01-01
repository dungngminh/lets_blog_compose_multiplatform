package me.dungngminh.lets_blog_kmp.data.models.response.document

import kotlinx.serialization.Serializable

@Serializable()
data class UploadDocumentResponse(
    val url: String,
)
