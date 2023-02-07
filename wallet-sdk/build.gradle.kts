// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  `maven-publish`
  alias(libs.plugins.dokka)
  signing
  alias(libs.plugins.kotlin.serialization)
  idea
  alias(libs.plugins.kotlin.multiplatform)
}

// fun DependencyHandler.testIntegrationImplementation(dependencyNotation: Any): Dependency? =
//  add("testIntegrationImplementation", dependencyNotation)
//
// sourceSets {
//  val testIntegration by creating {
//    compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
//    runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
//  }
// }
//
// configurations {
//  val testIntegrationImplementation by getting {
//    extendsFrom(configurations.testImplementation.get())
//  }
// }

kotlin {
  jvm {
    compilations.all { kotlinOptions.jvmTarget = "1.8" }
    withJava()
    testRuns["test"].executionTask.configure { useJUnitPlatform() }
  }
  js(BOTH) { browser { commonWebpackConfig { cssSupport.enabled = true } } }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(libs.coroutines.core)
        api(libs.kotlin.serialization.json)
        api(libs.kotlin.datetime)
        api(libs.kotlin.logging)
      }
    }
    val commonTest by getting {}
    val jvmMain by getting {
      dependencies {
        api(libs.java.stellar.sdk)
        api(libs.okhttp3)
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation(libs.coroutines.test)
        implementation(libs.kotlin.junit)
        implementation(libs.mockk)
        implementation(libs.okhttp3.mockserver)
        implementation(libs.google.gson)
        implementation(libs.logback.classic)
      }
    }
    val jsMain by getting
    val jsTest by getting
  }
}

// idea.module {
//  val testSources = testSourceDirs
//
//  testSources.addAll(project.sourceSets.getByName("testIntegration").kotlin.srcDirs)
//  testSources.addAll(project.sourceSets.getByName("testIntegration").resources.srcDirs)
//
//  testSourceDirs = testSources
// }
//
// val testIntegration by
//  tasks.register<Test>("integrationTest") {
//    useJUnitPlatform()
//
//    testClassesDirs = sourceSets.getByName("testIntegration").output.classesDirs
//    classpath = sourceSets.getByName("testIntegration").runtimeClasspath
//
//    mustRunAfter(tasks.test)
//  }
//
// tasks.check.get().dependsOn += testIntegration
//
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

// val sourcesJar by
//  tasks.registering(Jar::class) {
//    archiveClassifier.set("sources")
//    from(kotlin.sourceSets["jvmMain"].kotlin)
//  }

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
      //      artifact(sourcesJar)

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
