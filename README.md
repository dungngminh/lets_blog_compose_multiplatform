# Let's Blog - Read, Write and Share Blog platform

[![Github Pages deployment](https://github.com/dungngminh/lets_blog_compose_multiplatform/actions/workflows/build_web_app.yaml/badge.svg)](https://github.com/dungngminh/lets_blog_compose_multiplatform/actions/workflows/build_web_app.yaml)

<img alt="AppLogo.webp" height="100" src="art/img_app_icon.webp" width="100"/>

Blog Sharing Platform written in Compose Multiplatform, created
by [@dungngminh](https://github.com/dungngminh).

## Getting Started 🤖

- Everyone loves reading and sharing

## Feature set 🔥

- ✅ View all blogs, filter popular by favorite, filter blogs by category.
- ✅ Summary blog content with Gemini
- ✅ Create new blog with WYSIWYG editor.
- ✅ Search blogs by title, content,
- ✅ Save blog to favorite list.
- ✅ Authentication
- ✅ Manage user profile information, user's blogs

## Architecture 🏗️

This project
follows [Recommended app architecture](https://developer.android.com/topic/architecture#recommended-app-arch).

## What I used 💪

- [Compose Multiplatform UI Framework](https://www.jetbrains.com/compose-multiplatform/) - Shared UI
  code between Android, iOS, Web, Desktop with Compose UI.
- [Gemini](https://gemini.google.com) - Generative model from Google, used for summarizing blog
  content.
- [Voyager](https://github.com/adrielcafe/voyager) for navigation solution.
- [Koin](https://insert-koin.io/) for dependency injection.
- [Ktor](https://ktor.io/) via Ktorfit for networking.
- [Compose Rich Editor](https://github.com/MohamedRejeb/compose-rich-editor) for WYSIWYG editor.
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

### IDE Setup

### Prepare env configuration

- *Currently, I filled env for KotlinConf contest, you can use it for testing in
  `env.contest.properties`.*
- Copy `env.sample.properties` to `env.properties` and fill in your own configuration.
- Env information:
    - `GEMINI_KEY`: Gemini API key, how to get it, check this link
      to [Gemini](https://ai.google.dev/gemini-api/docs/api-key)
    - `BASE_URL`: Your backend base URL

## Contributors 🌟

<table>
  <tr>
    <td align="center"><img src="https://avatars.githubusercontent.com/u/63831488?v=4" width="100px;" alt=""/><br /><sub><b>Dzung Nguyen Minh</b></sub></td><br /><a href="https://github.com/dungngminh/app_creaty/commits?author=dungngminh" title="Maintainer">💻</a> <a title="Document">📖</a> <a title="Fix Bug">🐛</a>  
</tr>
</table>


Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack
channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them
on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).
