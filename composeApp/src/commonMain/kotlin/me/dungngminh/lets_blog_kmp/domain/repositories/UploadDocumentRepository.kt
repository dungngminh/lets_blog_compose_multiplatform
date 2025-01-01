package me.dungngminh.lets_blog_kmp.domain.repositories

import io.github.vinceglb.filekit.core.PlatformFile

interface UploadDocumentRepository {
    suspend fun uploadBlogImage(file: PlatformFile): Result<String>

    suspend fun uploadUserAvatar(file: PlatformFile): Result<String>
}
