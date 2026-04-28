import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.vanniktech.maven.publish)
  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlin.serialization)
  idea
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
  implementation(libs.bcutil)

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

mavenPublishing {
  // The plugin does NOT auto-detect Dokka; explicitly tell it to back the
  // javadoc JAR with the existing `dokkaHtml` task so the published
  // documentation matches what prior 2.x releases shipped.
  configure(
    KotlinJvm(
      javadocJar = JavadocJar.Dokka("dokkaHtml"),
      sourcesJar = true,
    )
  )

  publishToMavenCentral(automaticRelease = true)
  signAllPublications()

  coordinates("org.stellar", "wallet-sdk", project.version.toString())

  pom {
    name.set("Stellar Wallet SDK")
    description.set(
      "DEPRECATED — no longer maintained. " +
        "See https://github.com/stellar/kotlin-wallet-sdk for alternatives."
    )
    url.set("https://github.com/stellar/kotlin-wallet-sdk")
    inceptionYear.set("2022")

    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        distribution.set("repo")
      }
    }
    issueManagement {
      system.set("Github")
      url.set("https://github.com/stellar/kotlin-wallet-sdk/issues")
    }
    scm {
      connection.set("scm:git:git://github.com/stellar/kotlin-wallet-sdk.git")
      developerConnection.set("scm:git:ssh://git@github.com/stellar/kotlin-wallet-sdk.git")
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
      developer {
        id.set("CassioMG")
        name.set("Cassio")
        email.set("cassio@stellar.org")
      }
    }
  }
}