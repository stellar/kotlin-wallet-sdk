// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
  jvm {
    compilations.all { kotlinOptions.jvmTarget = "1.8" }
    withJava()
  }

  sourceSets {
    val jvmMain by getting {
        dependencies {
            implementation(project(":wallet-sdk"))

            implementation(libs.kotlin.serialization.json)
            implementation(libs.bundles.ktor.client)
        }
    }
  }
}
