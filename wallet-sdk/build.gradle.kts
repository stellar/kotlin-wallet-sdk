// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  `maven-publish`
  alias(libs.plugins.dokka)
}

dependencies {
  api(libs.coroutines.core)
  api(libs.java.stellar.sdk)
  api(libs.google.gson)
  api(libs.okhttp3)

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
