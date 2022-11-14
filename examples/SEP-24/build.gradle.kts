plugins {
  application
}

dependencies {
  // See gradle/libs.versions.toml for dependency resolution
  implementation(libs.coroutines.core)
  implementation(libs.java.stellar.sdk)
  implementation(libs.google.gson)
  implementation(libs.okhttp3)

  implementation(project(":wallet-sdk"))
}

application { mainClass.set("org.stellar.walletsdk.MainKt") }
