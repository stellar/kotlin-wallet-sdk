// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.kotlin.jvm)
}

val jvmVersion = JavaVersion.VERSION_1_8

allprojects {
  group = "org.stellar.wallet-sdk"
  version = "0.2.0"
}

subprojects {
  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "kotlin")

  repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://dl.bintray.com/palantir/releases") }
  }

  spotless {
    val javaVersion = JavaVersion.current()

    if (javaVersion >= JavaVersion.VERSION_17) {
      logger.warn("!!! WARNING !!!")
      logger.warn("=================")
      logger.warn(
        "    You are running Java version:[{}]. Spotless may not work well with JDK 17.",
        javaVersion
      )
      logger.warn(
        "    In IntelliJ, go to [File -> Build -> Execution, Build, Deployment -> Gradle] and check Gradle JVM"
      )
    }

    if (javaVersion < JavaVersion.VERSION_11) {
      throw GradleException("Java 11 or greater is required for spotless Gradle plugin.")
    }

    kotlin { ktfmt("0.39").googleStyle() }
  }

  dependencies {
    // Define common dependencies here
  }

  tasks {
    compileKotlin {
      dependsOn("spotlessKotlinApply")
      kotlinOptions.jvmTarget = jvmVersion.toString()
    }

    test { useJUnitPlatform() }
  }
}

tasks.register("printVersionName") {
  println(rootProject.version.toString())
}
