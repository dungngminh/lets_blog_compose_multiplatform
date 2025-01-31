https://github.com/user-attachments/assets/8637ecb4-20d6-430b-acd2-126ddf8f6639

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

# Let's Blog - Read, Write and Share Blog platform

<img alt="AppLogo.webp" height="100" src="art/img_app_icon.webp" width="100"/>

Read, Write and Share Blog platform written in Compose Multiplatform, created
by [@dungngminh](https://github.com/dungngminh).

## Getting Started 🤖

Everyone loves writing through their own stories, experiences, knowledge, lifehack,.. via blog and
so do I. I want to create a place for everyone **writing, sharing, reading blogs, anything, anyway,
anytime and cross-platform**. That's why I created this project. I hope this will be a good platform
for everyone. This platform is also integrated **Gemini AI** to help read more efficiently, no more
skimming through lines, find the needed information and also a good example of **Compose
Multiplatform**. And now, **Let's Blog!** 🚀

## Feature set 🔥

- ✅ Huge blogs list, popular blogs that filter by favorite count.
- ✅ Summary blog content with Gemini
- ✅ Create new blog with WYSIWYG interactive editor.
- ✅ Search blogs by title, content,
- ✅ Save blog to favorite list.
- ✅ Authentication
- ✅ Manage user profile information, user's blogs

## Architecture 🏗️

- This app shared UI, logic code between Android, iOS, Web, Desktop with Compose Multiplatform.

- Includes 2 main modules:

  - `composeApp`: Shared code between Android, iOS, Web, Desktop.
  - `backend`: Backend side written in Dart, using Dart Frog framework.

- App architecture
  follows [Recommended app architecture](https://developer.android.com/topic/architecture#recommended-app-arch) from Android.

![architecture](art/Architecture.webp)

## What I used 💪

- [Compose Multiplatform UI Framework](https://www.jetbrains.com/compose-multiplatform/) - Shared UI
  code between Android, iOS, Web, Desktop with Compose UI.
- [Gemini](https://gemini.google.com) - Generative model from Google, used for summarizing blog
  content.
- [Material 3](https://m3.material.io/) - Material design system.
- [Voyager](https://github.com/adrielcafe/voyager) for navigation solution.
- [Koin](https://insert-koin.io/) for dependency injection.
- [Ktor](https://ktor.io/) via Ktorfit for networking.
- [Compose Rich Editor](https://github.com/MohamedRejeb/compose-rich-editor) for WYSIWYG interactive
  editor.
- [multiplatform-settings](https://github.com/russhwolf/multiplatform-settings) for shared
  preferences.
- [Dart Frog](https://dartfrog.vgv.dev/) for backend side.
- [PostgreSQL](https://www.postgresql.org/) for backend database.
- [Railway](https://railway.app/) for hosting backend and database.
- [Github Pages](https://pages.github.com/) for hosting web app, auto deploy with Github Actions.

... and some utilities libraries like FlowExt, Landscapist, etc.

## Platform 📦

- ✅ Android
- ✅ iOS
- ✅ [Web Wasm](https://dungngminh.github.io/lets_blog_compose_multiplatform/)
- ✅ Desktop JVM

## How to run this project ❓

### First step

Clone this project to your local machine.

```shell
git clone https://github.com/dungngminh/lets_blog_compose_multiplatform.git
```

This project includes 4 run configurations:

- `composeApp`: Run Android app
- `iosApp`: Run iOS app
- `WebApp`: Run Web app
- `DesktopApp`: Run Desktop app

### Prepare env configuration

- _Currently, I filled env for KotlinConf contest, you can use it for testing
  in `env.contest.properties`._
- If you want to run this project, with your own configuration, you need to create env properties
  file, I also put a sample file is `env.sample.properties`, you can copy it to `env.properties` and
  fill your own configuration. Currently, source code is filled with `env.contest.properties` file,
  you can change it in `build.gradle.kts` file at `buildKonfig` block in `composeApp` module.

![buildKonfig](art/screenshots/buildkonfig.png)

- Env information:
  - `GEMINI_KEY`: Gemini API key
  - `BASE_URL`: Base URL for backend API

- Account test:
  - email: ngminhdung1311@gmail.com
  - password: kminhdung123

### Backend side

Backend side is written in Dart, using Dart Frog framework, you can find it in install
instruction [here](/backend/Readme.md) from `backend` folder.

### Android

Run `composeApp` via imported configuration.

### iOS

Run `iosApp` via imported configuration.

### Desktop

Run `DesktopApp` via imported configuration or run command:

```shell
./gradlew :desktopApp:run
```

### Web

Run `WebApp` via imported configuration or run command:

```shell
./gradlew :composeApp:wasmJsBrowserRun -t --quiet
```

## Screenshots 📸

### Android

<details>
<summary>Click to expand</summary>

| Home                                               | Search                                                               | Favorite                                                        |
| -------------------------------------------------- | -------------------------------------------------------------------- | --------------------------------------------------------------- |
| ![home](art/screenshots/phone_home.png)            | ![search](art/screenshots/phone_search.png)                          | ![favorite](art/screenshots/android_favorite.png)               |
| Detail Blog                                        | Profile                                                              | Blog Editor                                                     |
| ![detail](art/screenshots/phone_detail.png)        | ![profile](art/screenshots/android_profile.png)                      | ![editor](art/screenshots/phone_editor.png)                     |
| Preview Blog to upload                             | Login                                                                | Register                                                        |
| ![preview](art/screenshots/phone_preview_blog.png) | ![login](art/screenshots/phone_login.png)                            | ![register](art/screenshots/phone_register.png)                 |
| Summary Blog                                       |                                                                      |                                                                 |
| ![summary](art/screenshots/phone_gemini.png)       | ![summary_1](art/screenshots/android_summary_done_half_expanded.png) | ![summary_2](art/screenshots/android_summary_full_expanded.png) |

</details>

### iOS

<details>
<summary>Click to expand</summary>

| Home                                        | Search                                             | Favorite                                           |
| ------------------------------------------- | -------------------------------------------------- | -------------------------------------------------- |
| ![home](art/screenshots/ios_home.png)       | ![search](art/screenshots/ios_search.png)          | ![favorite](art/screenshots/ios_favorite.png)      |
| Detail Blog                                 | Profile                                            | Blog Editor                                        |
| ![detail](art/screenshots/ios_detail.png)   | ![profile](art/screenshots/ios_profile.png)        | ![editor](art/screenshots/ios_editor.png)          |
| Preview Blog to upload                      | Login                                              | Register                                           |
| ![preview](art/screenshots/ios_preview.png) | ![login](art/screenshots/ios_login.png)            | ![register](art/screenshots/ios_register.png)      |
| Summary Blog                                |                                                    |                                                    |
| ![summary](art/screenshots/ios_gemini.png)  | ![summary_1](art/screenshots/ios_summary_half.png) | ![summary_2](art/screenshots/ios_summary_full.png) |

</details>

### Desktop (MacOS)

<details>
<summary>Click to expand</summary>

| Home                                            | Search                                          | Favorite                                          |
| ----------------------------------------------- | ----------------------------------------------- | ------------------------------------------------- |
| ![home](art/screenshots/desktop_home.png)       | ![search](art/screenshots/desktop_search.png)   | ![favorite](art/screenshots/desktop_favorite.png) |
| Detail Blog                                     | Profile                                         | Blog Editor                                       |
| ![detail](art/screenshots/desktop_detail.png)   | ![profile](art/screenshots/desktop_profile.png) | ![editor](art/screenshots/desktop_editor.png)     |
| Preview Blog to upload                          | Login                                           | Register                                          |
| ![preview](art/screenshots/desktop_preview.png) | ![login](art/screenshots/desktop_login.png)     | ![register](art/screenshots/desktop_register.png) |
| Summary Blog                                    |                                                 |                                                   |
| ![summary](art/screenshots/desktop_summary.png) |                                                 |                                                   |

</details>

### Web

<details>
<summary>Click to expand</summary>

| Home                                                 | Search                                                  | Favorite                                      |
| ---------------------------------------------------- | ------------------------------------------------------- | --------------------------------------------- |
| ![home](art/screenshots/web_home.png)                | ![search](art/screenshots/web_search.png)               | ![favorite](art/screenshots/web_favorite.png) |
| Detail Blog                                          | Profile                                                 | Blog Editor                                   |
| ![detail](art/screenshots/web_detail.png)            | ![profile](art/screenshots/web_profile.png)             | ![editor](art/screenshots/web_editor.png)     |
| Preview Blog to upload                               | Login                                                   | Register                                      |
| ![preview](art/screenshots/web_preview.png)          | ![login](art/screenshots/web_login.png)                 | ![register](art/screenshots/web_register.png) |
| Summary Blog                                         |                                                         |                                               |
| ![summary](art/screenshots/web_blog_summarizing.png) | ![summary_1](art/screenshots/web_blog_summary_done.png) |                                               |

</details>

## Contributors 🌟

<table>
  <tr>
    <td align="center"><img src="https://avatars.githubusercontent.com/u/63831488?v=4" width="100px;" alt=""/><br /><sub><b>Dzung Nguyen Minh</b></sub></a><br /><a href="https://github.com/dungngminh/app_creaty/commits?author=dungngminh" title="Maintainer">💻</a> <a title="Document">📖</a> <a title="Fix Bug">🐛</a>  
</tr>
</table>

## More about Compose Multiplatform 🚀

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack
channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them
on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).
