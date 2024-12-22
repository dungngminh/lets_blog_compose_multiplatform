package me.dungngminh.lets_blog_kmp.data.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    @SerialName("confirmation_password")
    val confirmationPassword: String,
)
