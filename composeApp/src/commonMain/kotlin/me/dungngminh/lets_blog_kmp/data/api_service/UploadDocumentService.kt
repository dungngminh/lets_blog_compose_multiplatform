package me.dungngminh.lets_blog_kmp.data.api_service

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import io.ktor.http.content.PartData
import me.dungngminh.lets_blog_kmp.data.models.request.document.UploadDocumentRequest
import me.dungngminh.lets_blog_kmp.data.models.response.BaseResponse
import me.dungngminh.lets_blog_kmp.data.models.response.document.UploadDocumentResponse

interface UploadDocumentService {
    // I must create this function because I don't know whyyyyyyyyy my Dart server can't handle the file upload request via Multipart
    // I have tried postman, bruno or other API Client and it works perfectly but ktor is not :(((
    @Multipart
    @POST("api/documents/upload-base64")
    suspend fun uploadFileViaBase64(
        @Body body: UploadDocumentRequest,
    ): BaseResponse<UploadDocumentResponse>

    @Multipart
    @POST("api/documents/upload")
    suspend fun uploadFile(
        @Part("folderName") folderName: String,
        @Part("file") file: List<PartData>,
    ): BaseResponse<UploadDocumentResponse>
}
