package me.dungngminh.lets_blog_kmp.data.repositories

import com.hoc081098.flowext.flowFromSuspend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.dungngminh.lets_blog_kmp.data.api_service.UserService
import me.dungngminh.lets_blog_kmp.data.mappers.toEditUserRequest
import me.dungngminh.lets_blog_kmp.data.mappers.toUser
import me.dungngminh.lets_blog_kmp.domain.entities.User
import me.dungngminh.lets_blog_kmp.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userService: UserService,
    private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {
    override fun getUserProfile(id: String): Flow<Result<User>> =
        flowFromSuspend {
            runCatching {
                val response = userService.getUserProfile(id)
                val userResponse = response.unwrap()
                userResponse.toUser()
            }
        }.flowOn(ioDispatcher)

    override suspend fun editUserProfile(user: User): Result<Unit> =
        ioDispatcher.runCatching {
            userService.editUserProfile(
                id = user.id,
                editUserRequest = user.toEditUserRequest(),
            )
        }
}
