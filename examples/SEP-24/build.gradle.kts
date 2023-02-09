// The alias call in plugins scope produces IntelliJ false error which is suppressed here.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  application
}

kotlin {
  jvm {
    compilations.all { kotlinOptions.jvmTarget = "1.8" }
    withJava()
    testRuns["test"].executionTask.configure { useJUnitPlatform() }
  }

  sourceSets {
    val jvmMain by getting {
      dependencies {
         implementation(project(":wallet-sdk"))
      }
    }
  }
}

application { mainClass.set("org.stellar.walletsdk.MainKt") }
