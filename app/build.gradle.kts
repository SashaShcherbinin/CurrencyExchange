plugins {
    id(libs.plugins.common.android.application.module)
    alias(libs.plugins.kotlin.copmose)
}

object Version {
    private const val MAJOR = 1
    private const val MINOR = 0
    private const val PATCH = 0
    const val CODE = MAJOR * 10000 + MINOR * 100 + PATCH
    const val NAME = "$MAJOR.$MINOR.$PATCH"
}

android {
    namespace = "com.start"
    defaultConfig {
        applicationId = "currency.exchange"
        versionCode = Version.CODE
        versionName = Version.NAME
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.feature.splash.api)
    implementation(projects.feature.splash.impl)
    implementation(projects.feature.dashboard.api)
    implementation(projects.feature.dashboard.impl)
    implementation(projects.feature.currency.api)
    implementation(projects.feature.currency.impl)
    implementation(projects.feature.account.api)
    implementation(projects.feature.account.impl)
    implementation(projects.base.logger.api)
    implementation(projects.base.logger.impl)
    implementation(projects.base.network.impl)
    implementation(projects.base.storage.impl)
    implementation(projects.base.compose.impl)
    implementation(projects.base.domain.api)

    implementation(libs.google.material)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.koin.android)
}