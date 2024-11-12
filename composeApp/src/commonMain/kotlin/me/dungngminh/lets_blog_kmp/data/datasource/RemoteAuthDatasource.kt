package me.dungngminh.lets_blog_kmp.data.datasource

import io.ktor.client.HttpClient

class RemoteAuthDatasource(
    private val httpClient: HttpClient,
) {
    suspend fun login(
        email: String,
        password: String,
    ) {
    }
}
