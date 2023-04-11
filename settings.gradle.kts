rootProject.name = "kotlin-wallet-sdk"

/** SDK */
include("wallet-sdk")


fun example(name: String) {
    include(name)
    project(":$name").projectDir = file("examples/$name")
}

/** examples */
example("SEP-24")
example("MGI")
example("recovery")
example("documentation")