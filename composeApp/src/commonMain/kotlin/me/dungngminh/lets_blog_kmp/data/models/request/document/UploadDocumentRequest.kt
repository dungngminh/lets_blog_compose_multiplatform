package me.dungngminh.lets_blog_kmp.data.models.request.document

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadDocumentRequest(
    @SerialName("folder_name") val folderName: String,
    @SerialName("file_name") val fileName: String,
    @SerialName("base64_source") val base64Source: String,
)
