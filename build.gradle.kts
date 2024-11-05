// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.detekt)
}

apply(plugin = "base")

val jvmVersion = JavaVersion.VERSION_11

allprojects {
  group = "org.stellar.wallet-sdk"
  version = "1.7.2"
}

subprojects {
  val subProject = this

  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "kotlin")
  apply(plugin = "io.gitlab.arturbosch.detekt")

  repositories {
    mavenLocal()
    mavenCentral()
  }

  // Do not apply spotless and detekt to auto-generated files
  if (subProject.name != "documentation") {
    spotless {
      val javaVersion = JavaVersion.current()

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

  dependencies {
    // Define common dependencies here
  }

  tasks {
    compileKotlin {
      // Ignore spotless for auto-generated files
      if (subProject.name != "documentation") {
        dependsOn("spotlessKotlinApply")
      }
      kotlinOptions.jvmTarget = jvmVersion.toString()
      compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.io.encoding.ExperimentalEncodingApi")
      }
    }

    compileTestKotlin {
      kotlinOptions.jvmTarget = jvmVersion.toString()
      compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.io.encoding.ExperimentalEncodingApi")
      }
    }

    test { useJUnitPlatform() }
  }

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
