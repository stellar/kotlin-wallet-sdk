package org.stellar.walletsdk

import com.palantir.docker.compose.DockerComposeExtension
import org.junit.jupiter.api.Test

// TODO: Can use DOCKER_COMPOSE_LOCATION env to define custom docker-compose location, by default
// it's looking at /usr/local/bin/docker-compose

class Test {
  var docker: DockerComposeExtension =
    DockerComposeExtension.builder()
      .file("src/testIntegration/resources/docker-compose.yml")
      .build()

  @Test
  fun testOk() {
    println("OK")
  }
}
