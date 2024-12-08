plugins {
    id(libs.plugins.common.android.library.module)
    alias(libs.plugins.kotlin.copmose)
}

android {
    namespace = "feature.splash"
    resourcePrefix("splash")
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.coroutines.core)
    implementation(libs.koin.core)

    implementation(projects.base.domain.api)
    implementation(projects.base.compose.impl)
    implementation(projects.feature.dashboard.api)
    implementation(projects.feature.splash.api)
}
