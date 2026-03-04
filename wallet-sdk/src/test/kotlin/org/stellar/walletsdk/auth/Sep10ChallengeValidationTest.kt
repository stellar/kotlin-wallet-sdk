package org.stellar.walletsdk.auth

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import java.math.BigDecimal
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.stellar.sdk.*
import org.stellar.sdk.operations.ManageDataOperation
import org.stellar.sdk.operations.PaymentOperation
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.ChallengeClientAccountMismatchException
import org.stellar.walletsdk.exception.DomainSigningModifiedException
import org.stellar.walletsdk.exception.InvalidChallengeException
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair

internal class Sep10ChallengeValidationTest : SuspendTest() {
  private val network = Network.TESTNET
  private val homeDomain = "testanchor.example.com"
  private val webAuthEndpoint = "https://testanchor.example.com/auth"
  private val webAuthDomain = "testanchor.example.com"

  private val serverKeyPair = KeyPair.random()
  private val serverSigningKey = serverKeyPair.accountId

  private val clientKeyPair = KeyPair.random()
  private val clientSigningKeyPair = SigningKeyPair(clientKeyPair)

  private fun validChallenge(
    clientAccount: String = clientKeyPair.accountId,
    timeBounds: TimeBounds = TimeBounds.expiresAfter(300)
  ): Transaction {
    return Sep10Challenge.newChallenge(
      serverKeyPair,
      network,
      clientAccount,
      homeDomain,
      webAuthDomain,
      timeBounds
    )
  }

  private fun challengeXdr(txn: Transaction): String = txn.toEnvelopeXdrBase64()

  private fun buildMockClient(challengeXdr: String): HttpClient {
    val challengeJson =
      Json.encodeToString(ChallengeResponse(challengeXdr, network.networkPassphrase))
    @Suppress("MaxLineLength")
    val tokenJson =
      """{"token":"eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0IiwiZXhwIjo5OTk5OTk5OTk5LCJpYXQiOjE2MDAwMDAwMDAsInN1YiI6IiR7Y2xpZW50S2V5UGFpci5hY2NvdW50SWR9In0.test"}"""

    val mockEngine = MockEngine { request ->
      when (request.method) {
        HttpMethod.Get ->
          respond(
            content = ByteReadChannel(challengeJson),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
          )
        HttpMethod.Post ->
          respond(
            content = ByteReadChannel(tokenJson),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
          )
        else -> error("Unexpected method ${request.method}")
      }
    }
    return HttpClient(mockEngine) { install(ContentNegotiation) { json() } }
  }

  private fun sep10(httpClient: HttpClient): Sep10 {
    return Sep10(TestWallet.cfg, webAuthEndpoint, serverSigningKey, homeDomain, httpClient)
  }

  @Test
  fun `valid challenge passes validation`() {
    val challenge = validChallenge()
    val client = buildMockClient(challengeXdr(challenge))

    runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
  }

  @Test
  fun `rejects challenge with non-zero sequence number`() {
    val serverAccount = Account(serverKeyPair.accountId, 42L)
    val txn =
      TransactionBuilder(serverAccount, network)
        .setBaseFee(100)
        .setTimeout(300)
        .addOperation(
          ManageDataOperation.builder()
            .name("$homeDomain auth")
            .value(ByteArray(48))
            .sourceAccount(clientKeyPair.accountId)
            .build()
        )
        .addOperation(
          ManageDataOperation.builder()
            .name("web_auth_domain")
            .value(webAuthDomain.toByteArray())
            .sourceAccount(serverKeyPair.accountId)
            .build()
        )
        .build()
    txn.sign(serverKeyPair)

    val client = buildMockClient(challengeXdr(txn))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge with payment operation`() {
    val serverAccount = Account(serverKeyPair.accountId, -1L)
    val txn =
      TransactionBuilder(serverAccount, network)
        .setBaseFee(100)
        .setTimeout(300)
        .addOperation(
          PaymentOperation.builder()
            .destination(serverKeyPair.accountId)
            .asset(AssetTypeNative())
            .amount(BigDecimal("100"))
            .sourceAccount(clientKeyPair.accountId)
            .build()
        )
        .build()
    txn.sign(serverKeyPair)

    val client = buildMockClient(challengeXdr(txn))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge from wrong server`() {
    val wrongServerKeyPair = KeyPair.random()
    val challenge =
      Sep10Challenge.newChallenge(
        wrongServerKeyPair,
        network,
        clientKeyPair.accountId,
        homeDomain,
        webAuthDomain,
        TimeBounds.expiresAfter(300)
      )
    val client = buildMockClient(challengeXdr(challenge))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge without server signature`() {
    val serverAccount = Account(serverKeyPair.accountId, -1L)
    val unsignedChallenge =
      TransactionBuilder(serverAccount, network)
        .setBaseFee(100)
        .setTimeout(300)
        .addOperation(
          ManageDataOperation.builder()
            .name("$homeDomain auth")
            .value(ByteArray(48))
            .sourceAccount(clientKeyPair.accountId)
            .build()
        )
        .addOperation(
          ManageDataOperation.builder()
            .name("web_auth_domain")
            .value(webAuthDomain.toByteArray())
            .sourceAccount(serverKeyPair.accountId)
            .build()
        )
        .build()

    val client = buildMockClient(challengeXdr(unsignedChallenge))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge with expired timebounds`() {
    val challenge =
      Sep10Challenge.newChallenge(
        serverKeyPair,
        network,
        clientKeyPair.accountId,
        homeDomain,
        webAuthDomain,
        TimeBounds(0, 1)
      )

    val client = buildMockClient(challengeXdr(challenge))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge with wrong home domain`() {
    val challenge =
      Sep10Challenge.newChallenge(
        serverKeyPair,
        network,
        clientKeyPair.accountId,
        "evil.example.com",
        webAuthDomain,
        TimeBounds.expiresAfter(300)
      )

    val client = buildMockClient(challengeXdr(challenge))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge for wrong client account`() {
    val otherClient = KeyPair.random()
    val challenge =
      Sep10Challenge.newChallenge(
        serverKeyPair,
        network,
        otherClient.accountId,
        homeDomain,
        webAuthDomain,
        TimeBounds.expiresAfter(300)
      )

    val client = buildMockClient(challengeXdr(challenge))

    assertThrows<ChallengeClientAccountMismatchException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge with wrong web_auth_domain`() {
    val challenge =
      Sep10Challenge.newChallenge(
        serverKeyPair,
        network,
        clientKeyPair.accountId,
        homeDomain,
        "evil.example.com",
        TimeBounds.expiresAfter(300)
      )

    val client = buildMockClient(challengeXdr(challenge))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects challenge with extra non-ManageData operation`() {
    val serverAccount = Account(serverKeyPair.accountId, -1L)
    val txn =
      TransactionBuilder(serverAccount, network)
        .setBaseFee(100)
        .setTimeout(300)
        .addOperation(
          ManageDataOperation.builder()
            .name("$homeDomain auth")
            .value(ByteArray(48))
            .sourceAccount(clientKeyPair.accountId)
            .build()
        )
        .addOperation(
          ManageDataOperation.builder()
            .name("web_auth_domain")
            .value(webAuthDomain.toByteArray())
            .sourceAccount(serverKeyPair.accountId)
            .build()
        )
        .addOperation(
          PaymentOperation.builder()
            .destination(serverKeyPair.accountId)
            .asset(AssetTypeNative())
            .amount(BigDecimal("1000"))
            .sourceAccount(clientKeyPair.accountId)
            .build()
        )
        .build()
    txn.sign(serverKeyPair)

    val client = buildMockClient(challengeXdr(txn))

    assertThrows<InvalidChallengeException> {
      runBlocking { sep10(client).authenticate(clientSigningKeyPair) }
    }
  }

  @Test
  fun `rejects domain signer that modifies transaction body`() {
    val tamperingServerKp = KeyPair.random()
    val tamperingChallenge =
      Sep10Challenge.newChallenge(
        tamperingServerKp,
        network,
        clientKeyPair.accountId,
        "evil.com",
        "evil.com",
        TimeBounds.expiresAfter(300)
      )
    val tamperingSigner =
      object : WalletSigner.DefaultSigner() {
        override suspend fun signWithDomainAccount(
          transactionXDR: String,
          networkPassPhrase: String,
          account: AccountKeyPair
        ): Transaction {
          return tamperingChallenge
        }
      }

    val challengeWithClientDomain =
      Sep10Challenge.newChallenge(
        serverKeyPair,
        network,
        clientKeyPair.accountId,
        homeDomain,
        webAuthDomain,
        TimeBounds.expiresAfter(300),
        "client.example.com",
        KeyPair.random().accountId
      )
    val clientWithDomain = buildMockClient(challengeXdr(challengeWithClientDomain))

    assertThrows<DomainSigningModifiedException> {
      runBlocking {
        sep10(clientWithDomain).authenticate(clientSigningKeyPair, walletSigner = tamperingSigner)
      }
    }
  }
}
