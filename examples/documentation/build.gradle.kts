// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
}

sourceSets.main { java.srcDirs("src") }

dependencies {
    implementation(project(":wallet-sdk"))

    implementation(libs.kotlin.serialization.json)
    implementation(libs.bundles.ktor.client)
}
