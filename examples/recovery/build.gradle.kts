plugins { application }

dependencies {
  implementation(project(":wallet-sdk"))
  implementation(libs.hoplite.core)
  implementation(libs.hoplite.yaml)
  implementation("com.google.firebase:firebase-admin:9.1.1")
}

repositories {
  google()
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
  }
}

application { mainClass.set("org.stellar.walletsdk.MainKt") }
