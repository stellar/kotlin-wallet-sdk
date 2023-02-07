// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.detekt)
}

buildscript { dependencies { classpath(libs.knit) } }

apply(plugin = "base")

apply(plugin = "kotlinx-knit")

val jvmVersion = JavaVersion.VERSION_1_8

allprojects {
  group = "org.stellar.wallet-sdk"
  version = "0.4.0"
}

subprojects {
  val subProject = this

  apply(plugin = "com.diffplug.spotless")
//  apply(plugin = "kotlin")
  apply(plugin = "io.gitlab.arturbosch.detekt")

  repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
  }

  // Do not apply spotless and detekt to auto-generated files
  if (subProject.name != "documentation") {
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

    detekt {
      toolVersion = "1.22.0"
      config = files("$rootDir/config/detekt/detekt.yml")
      buildUponDefaultConfig = true
    }
  }

//  tasks {
//    compileKotlin {
//      // Ignore spotless for auto-generated files
//      if (subProject.name != "documentation") {
//        dependsOn("spotlessKotlinApply")
//      }
//      kotlinOptions.jvmTarget = jvmVersion.toString()
//    }
//
//    test { useJUnitPlatform() }
//  }

  tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
      xml.required.set(false)
      html.required.set(true)
      txt.required.set(false)
      sarif.required.set(false)
      md.required.set(false)
    }
  }
}

tasks.register("printVersionName") { println(rootProject.version.toString()) }
