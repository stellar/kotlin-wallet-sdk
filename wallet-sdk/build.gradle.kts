import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  `maven-publish`
  alias(libs.plugins.dokka)
  signing
  alias(libs.plugins.kotlin.serialization)
  idea
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

fun DependencyHandler.testIntegrationImplementation(dependencyNotation: Any): Dependency? =
    add("testIntegrationImplementation", dependencyNotation)

sourceSets {
  val testIntegration by creating {
    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
  }
}

configurations {
  val testIntegrationImplementation by getting {
    extendsFrom(configurations.testImplementation.get())
  }
}

dependencies {
  api(libs.coroutines.core)
  api(libs.java.stellar.sdk)
  api(libs.kotlin.datetime)
  api(libs.bundles.ktor.client)
  implementation(libs.kotlin.serialization.json)
  implementation(libs.kotlin.logging)
  implementation(libs.google.gson)
  implementation(libs.toml4j)
  implementation(libs.jjwt)
  implementation(libs.bcastle)

  testImplementation(libs.coroutines.test)
  testImplementation(libs.kotlin.junit)
  testImplementation(libs.mockk)
  testImplementation(libs.okhttp3.mockserver)
  testImplementation(libs.logback.classic)
  testImplementation(libs.ktor.client.mock)

  testIntegrationImplementation(libs.ktor.client.core)
  testIntegrationImplementation(libs.ktor.client.okhttp)
}

idea.module {
  val testSources = testSourceDirs

  testSources.addAll(project.sourceSets.getByName("testIntegration").kotlin.srcDirs)
  testSources.addAll(project.sourceSets.getByName("testIntegration").resources.srcDirs)

  testSourceDirs = testSources
}

val testIntegration by
    tasks.register<Test>("integrationTest") {
      useJUnitPlatform()

      testClassesDirs = sourceSets.getByName("testIntegration").output.classesDirs
      classpath = sourceSets.getByName("testIntegration").runtimeClasspath

      mustRunAfter(tasks.test)
    }

tasks.getByName("compileTestIntegrationKotlin") {
  (this as KotlinCompile).kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

tasks.check.get().dependsOn += testIntegration

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

val sourcesJar by
    tasks.registering(Jar::class) {
      archiveClassifier.set("sources")
      from(kotlin.sourceSets.main.get().kotlin)
    }

val uberJar =
    tasks.register<ShadowJar>("uberJar") {
      archiveBaseName.set("kotlin-wallet-sdk-uber")
      archiveClassifier.set("") // No classifier

      // Include all runtime dependencies
      from(sourceSets.main.get().output)
      configurations = listOf(project.configurations.runtimeClasspath.get())

      // Relocations
      relocate("com", "shadow.wallet-sdk.com")
      relocate("io", "shadow.wallet-sdk.io")
      relocate("mu", "shadow.wallet-sdk.mu")
      relocate("net", "shadow.wallet-sdk.net")
      relocate("okhttp3", "shadow.wallet-sdk.okhttp3")
      relocate("okio", "shadow.wallet-sdk.okio")
      relocate("org", "shadow.wallet-sdk.org") { exclude("org.stellar.walletsdk") }
    }

tasks {
  build {
    dependsOn(javadocJar)
    dependsOn(sourcesJar)
    dependsOn(uberJar)
  }
}

publishing {
  repositories {
    maven {
      name = "oss"
      val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
      val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
      url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
      credentials {
        username = System.getenv("OSSRH_USER") ?: return@credentials
        password = System.getenv("OSSRH_PASSWORD") ?: return@credentials
      }
    }
  }

  publications {
    create<MavenPublication>("mavenJava") {
      groupId = "org.stellar"
      artifactId = "wallet-sdk"

      from(components["java"])
      artifact(javadocJar)
      artifact(sourcesJar)

      pom {
        name.set("Stellar Wallet SDK")
        description.set("Kotlin Stellar Wallet SDK")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        url.set("https://github.com/stellar/kotlin-wallet-sdk")
        issueManagement {
          system.set("Github")
          url.set("https://github.com/stellar/kotlin-wallet-sdk/issues")
        }
        scm {
          connection.set("https://github.com/stellar/kotlin-wallet-sdk.git")
          url.set("https://github.com/stellar/kotlin-wallet-sdk")
        }
        developers {
          developer {
            id.set("Ifropc")
            name.set("Gleb")
            email.set("gleb@stellar.org")
          }
          developer {
            id.set("quietbits")
            name.set("Iveta")
            email.set("iveta@stellar.org")
          }
        }
      }
    }
  }

  apply<SigningPlugin>()
  configure<SigningExtension> {
    useGpgCmd()
    sign(publishing.publications)
  }
}
