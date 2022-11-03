// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  `maven-publish`
  alias(libs.plugins.dokka)
}

dependencies {
  implementation(libs.coroutines.core)
  implementation(libs.java.stellar.sdk)
  implementation(libs.google.gson)
  implementation(libs.okhttp3)

  testImplementation(libs.coroutines.test)
  testImplementation(libs.kotlin.junit)
  testImplementation(libs.mockk)
  testImplementation(libs.okhttp3.mockserver)
}

val dokkaOutputDir = buildDir.resolve("dokka")

tasks.dokkaHtml { outputDirectory.set(dokkaOutputDir) }

val deleteDokkaOutputDir by
  tasks.register<Delete>("deleteDokkaOutputDirectory") { delete(dokkaOutputDir) }

val javadocJar =
  tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
  }

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      groupId = "org.stellar"
      artifactId = "wallet-sdk"

      from(components["java"])
    }
  }
}
