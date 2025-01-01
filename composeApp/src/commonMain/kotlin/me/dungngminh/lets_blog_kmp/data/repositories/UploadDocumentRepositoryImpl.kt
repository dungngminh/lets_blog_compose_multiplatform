package me.dungngminh.lets_blog_kmp.data.repositories

import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.dungngminh.lets_blog_kmp.commons.extensions.toByteArray
import me.dungngminh.lets_blog_kmp.data.api_service.UploadDocumentService
import me.dungngminh.lets_blog_kmp.data.models.request.document.UploadDocumentRequest
import me.dungngminh.lets_blog_kmp.domain.repositories.UploadDocumentRepository
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class UploadDocumentRepositoryImpl(
    private val uploadDocumentService: UploadDocumentService,
    private val ioDispatcher: CoroutineDispatcher,
) : UploadDocumentRepository {
    override suspend fun uploadUserAvatar(file: PlatformFile): Result<String> =
        uploadDocument(
            file = file,
            folderName = USER_FOLDER_NAME,
        )

    override suspend fun uploadBlogImage(file: PlatformFile): Result<String> =
        uploadDocument(
            file = file,
            folderName = BLOG_FOLDER_NAME,
        )

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun uploadDocument(
        file: PlatformFile,
        folderName: String,
    ): Result<String> =
        withContext(ioDispatcher) {
            runCatching {
                val imageBytes = file.toByteArray()
                val base64Img = Base64.encode(imageBytes)
                val response =
                    uploadDocumentService.uploadFileViaBase64(
                        UploadDocumentRequest(
                            fileName = file.name,
                            folderName = folderName,
                            base64Source = base64Img,
                        ),
                    )
                response.unwrap().url
            }
        }

    companion object {
        const val BLOG_FOLDER_NAME = "blogs"
        const val USER_FOLDER_NAME = "users"
    }
}
