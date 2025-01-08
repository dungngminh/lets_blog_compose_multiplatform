import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.googleKsp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.buildKonfig)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        static =
                            (static ?: mutableListOf()).apply {
                                // Serve sources to debug inside browser
                                add(rootDirPath)
                                add(projectDirPath)
                            }
                    }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            // Windows Class
            implementation(libs.compose.material3.windowSizeClass)
            // Adaptive Navigation
            implementation(libs.compose.material3.adaptiveNavigationSuite)

            implementation(libs.androidx.lifecycle.runtime.compose)

            // ViewModel
            implementation(libs.androidx.lifecycle.viewmodel.compose)

            // Navigation
            implementation(libs.androidx.navigation.compose)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.shared)

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

            // Koin Annotations
            implementation(project.dependencies.platform(libs.koin.annotations.bom))
            implementation(libs.koin.annotations)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Lottie
            implementation(libs.compottie)
            implementation(libs.compottie.resources)
            implementation(libs.compottie.dot)

            // Napier
            implementation(libs.napier)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)

            // Ktorfit
            implementation(libs.ktorfit)

            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.transitions)

            // Flow Ext
            implementation(libs.flowExt)

            // Multiplatform Settings
            implementation(libs.multiplatformSettings.noArg)
            implementation(libs.multiplatformSettings.coroutines)
            implementation(libs.multiplatformSettings.makeObservable)

            // Datetime
            implementation(libs.kotlinx.datetime)

            // Immutable Collections
            implementation(libs.kotlinx.collections.immutable)

            // Coil
            implementation(libs.landscapist.coil3)

            // WYSIWYG
            implementation(libs.richeditor.compose)

            // Filekit
            implementation(libs.filekit.compose)

            // Gemini
            implementation(libs.generativeAi)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }

    // KSP Common sourceSet
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

// KSP Tasks
dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspIosX64", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}

// Trigger Common Metadata Generation from Native tasks
project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

android {
    namespace = "me.dungngminh.lets_blog_kmp"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "me.dungngminh.lets_blog_kmp"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "me.dungngminh.lets_blog_kmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "me.dungngminh.lets_blog_kmp"
            packageVersion = "1.0.0"
            linux {
                modules("jdk.security.auth")
            }
        }
    }
}

buildkonfig {
    packageName = "me.dungngminh.lets_blog_kmp"

    defaultConfigs {
        val envProperties = readProperty("env.properties") ?: return@defaultConfigs
        buildConfigField(STRING, "BASE_URL", envProperties.getProperty("BASE_URL"))
        buildConfigField(STRING, "GEMINI_KEY", envProperties.getProperty("GEMINI_KEY"))
    }
    defaultConfigs("dev") {
        val envProperties = readProperty("env.dev.properties") ?: return@defaultConfigs
        buildConfigField(STRING, "BASE_URL", envProperties.getProperty("BASE_URL"))
        buildConfigField(STRING, "GEMINI_KEY", envProperties.getProperty("GEMINI_KEY"))
    }
}

fun readProperty(fileName: String): Properties? {
    val localProperties = Properties()
    val file = project.rootProject.file(fileName)
    if (file.exists()) {
        localProperties.load(file.inputStream())
        return localProperties
    } else {
        return null
    }
}
