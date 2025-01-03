package me.dungngminh.lets_blog_kmp.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.dungngminh.lets_blog_kmp.data.api_service.UserService
import me.dungngminh.lets_blog_kmp.data.mappers.toBlog
import me.dungngminh.lets_blog_kmp.data.mappers.toEditUserRequest
import me.dungngminh.lets_blog_kmp.data.mappers.toUser
import me.dungngminh.lets_blog_kmp.domain.entities.Blog
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userService: UserService,
    private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {
    override suspend fun getUserProfile(id: String): Result<User> =
        withContext(ioDispatcher) {
            runCatching {
                val response = userService.getUserProfile(id)
                val userResponse = response.unwrap()
                userResponse.toUser()
            }
        }

    override suspend fun editUserProfile(user: User): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                userService.editUserProfile(
                    id = user.id,
                    editUserRequest = user.toEditUserRequest(),
                )
            }
        }

    override suspend fun getUserBlogs(id: String): Result<List<Blog>> =
        withContext(ioDispatcher) {
            runCatching {
                val response = userService.getUserBlogs(id)
                response
                    .unwrap()
                    .map { it.toBlog() }
            }
        }
}
