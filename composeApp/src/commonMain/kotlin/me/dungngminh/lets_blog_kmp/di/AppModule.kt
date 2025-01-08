package me.dungngminh.lets_blog_kmp.di

import me.dungngminh.lets_blog_kmp.AppViewModel
import me.dungngminh.lets_blog_kmp.presentation.detail_blog.DetailBlogViewModel
import me.dungngminh.lets_blog_kmp.presentation.edit_user_profile.EditUserProfileViewModel
import me.dungngminh.lets_blog_kmp.presentation.login.LoginViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.UserSessionViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.FavoriteViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.home.HomeScreenViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.profile.ProfileViewModel
import me.dungngminh.lets_blog_kmp.presentation.main.search.SearchViewModel
import me.dungngminh.lets_blog_kmp.presentation.preview_blog.PreviewBlogViewModel
import me.dungngminh.lets_blog_kmp.presentation.register.RegisterViewModel
import org.koin.dsl.module
import kotlin.jvm.JvmField

private val ViewModelModule =
    module {
        factory { AppViewModel(get()) }
        factory { LoginViewModel(get()) }
        factory { RegisterViewModel(get()) }
        factory {
            UserSessionViewModel(
                authRepository = get(),
                userRepository = get(),
            )
        }
        factory {
            HomeScreenViewModel(
                blogRepository = get(),
                favoriteRepository = get(),
            )
        }
        factory { params ->
            PreviewBlogViewModel(
                content = params.get(),
                blog = params.getOrNull(),
                blogRepository = get(),
                uploadDocumentRepository = get(),
            )
        }
        factory { params ->
            DetailBlogViewModel(
                blog = params.get(),
                blogRepository = get(),
                favoriteRepository = get(),
                summaryContentRepository = get(),
            )
        }

        factory {
            SearchViewModel(
                blogRepository = get(),
            )
        }
        factory {
            ProfileViewModel(
                authRepository = get(),
                userRepository = get(),
            )
        }
        factory {
            FavoriteViewModel(get())
        }
        factory { param ->
            EditUserProfileViewModel(
                user = param.get(),
                userRepository = get(),
                uploadDocumentRepository = get(),
            )
        }
    }

@JvmField
internal val AppModule =
    module {
        includes(DispatcherModule)
        includes(DataModule)
        includes(DomainModule)
        includes(ViewModelModule)
    }
