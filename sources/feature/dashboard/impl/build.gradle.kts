plugins {
    id(libs.plugins.common.android.library.module)
    alias(libs.plugins.kotlin.copmose)
}

android {
    namespace = "feature.dashboard"
    resourcePrefix("dashboard")
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.coroutines.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    debugImplementation(libs.compose.tooling)

    implementation(projects.base.compose.impl)
    implementation(projects.base.domain.api)
    implementation(projects.base.logger.api)
    implementation(projects.feature.dashboard.api)
    implementation(projects.feature.currency.api)
    implementation(projects.feature.account.api)
}
