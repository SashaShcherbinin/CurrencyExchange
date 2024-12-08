plugins {
    id(libs.plugins.common.android.library.module)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.copmose)
}

android {
    namespace = "feature.account"
    resourcePrefix("account")
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.bundles.network)
    implementation(libs.coroutines.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.compose.tooling)

    implementation(projects.base.compose.impl)
    implementation(projects.base.domain.api)
    implementation(projects.base.logger.api)
    implementation(projects.base.storage.impl)
    implementation(projects.feature.account.api)
    implementation(projects.feature.dashboard.api)
}
