plugins {
  application
}

dependencies {
  implementation(project(":wallet-sdk"))
  // To use in your project
  // implementation("org.stellar:wallet-sdk:0.1.0")
}

application { mainClass.set("org.stellar.example.MainKt") }
