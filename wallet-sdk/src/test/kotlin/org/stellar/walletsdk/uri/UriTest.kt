package org.stellar.walletsdk.uri

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import java.net.URL
import java.net.URLEncoder
import java.util.Base64
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.sdk.xdr.TransactionEnvelope
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.horizon.toPublicKeyPair
import org.stellar.walletsdk.toml.StellarToml
import org.stellar.walletsdk.toml.TomlInfo

class UriTest {

  companion object {
    // Transaction XDR used in multiple tests
    private const val TX_XDR =
      "AAAAAgAAAACM6IR9GHiRoVVAO78JJNksy2fKDQNs2jBn8bacsRLcrDucaFsAAAWIAAAAMQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAGAAAAAAAAAABHkEVdJ%2BUfDnWpBr%2FqF582IEoDQ0iW0WPzO9CEUdvvh8AAAAIdHJhbnNmZXIAAAADAAAAEgAAAAAAAAAAjOiEfRh4kaFVQDu%2FCSTZLMtnyg0DbNowZ%2FG2nLES3KwAAAASAAAAAAAAAADoFl2ACT9HZkbCeuaT9MAIdStpdf58wM3P24nl738AnQAAAAoAAAAAAAAAAAAAAAAAAAAFAAAAAQAAAAAAAAAAAAAAAR5BFXSflHw51qQa%2F6hefNiBKA0NIltFj8zvQhFHb74fAAAACHRyYW5zZmVyAAAAAwAAABIAAAAAAAAAAIzohH0YeJGhVUA7vwkk2SzLZ8oNA2zaMGfxtpyxEtysAAAAEgAAAAAAAAAA6BZdgAk%2FR2ZGwnrmk%2FTACHUraXX%2BfMDNz9uJ5e9%2FAJ0AAAAKAAAAAAAAAAAAAAAAAAAABQAAAAAAAAABAAAAAAAAAAIAAAAGAAAAAR5BFXSflHw51qQa%2F6hefNiBKA0NIltFj8zvQhFHb74fAAAAFAAAAAEAAAAHa35L%2B%2FRxV6EuJOVk78H5rCN%2BeubXBWtsKrRxeLnnpRAAAAACAAAABgAAAAEeQRV0n5R8OdakGv%2BoXnzYgSgNDSJbRY%2FM70IRR2%2B%2BHwAAABAAAAABAAAAAgAAAA8AAAAHQmFsYW5jZQAAAAASAAAAAAAAAACM6IR9GHiRoVVAO78JJNksy2fKDQNs2jBn8bacsRLcrAAAAAEAAAAGAAAAAR5BFXSflHw51qQa%2F6hefNiBKA0NIltFj8zvQhFHb74fAAAAEAAAAAEAAAACAAAADwAAAAdCYWxhbmNlAAAAABIAAAAAAAAAAOgWXYAJP0dmRsJ65pP0wAh1K2l1%2FnzAzc%2FbieXvfwCdAAAAAQBkcwsAACBwAAABKAAAAAAAAB1kAAAAAA%3D%3D"
  }

  private lateinit var testKeypair1: KeyPair
  private lateinit var testKeypair2: KeyPair

  @BeforeEach
  fun setup() {
    testKeypair1 =
      KeyPair.fromSecretSeed("SBKQDF56C5VY2YQTNQFGY7HM6R3V6QKDUEDXZQUCPQOP2EBZWG2QJ2JL")
    testKeypair2 =
      KeyPair.fromSecretSeed("SBIK5MF5QONDTKA5ZPXLI2XTBIAOWQEEOZ3TM76XVBPPJ2EEUUXTCIVZ")
  }

  @Test
  fun `constructor accepts string URI`() {
    val uriStr = "web+stellar:tx?xdr=test&callback=https%3A%2F%2Fexample.com%2Fcallback"
    val uri = Sep7Tx(uriStr)

    assertEquals(Sep7OperationType.TX, uri.operationType)
    assertEquals("test", uri.xdr)
    assertEquals("https://example.com/callback", uri.callback)
    assertEquals(uriStr, uri.toString())
  }

  @Test
  fun `should default to public network if not set`() {
    val uri = Sep7Tx("web+stellar:tx")
    assertEquals(Network.PUBLIC, uri.networkPassphrase)

    uri.networkPassphrase = Network.TESTNET
    assertEquals(Network.TESTNET, uri.networkPassphrase)
  }

  @Test
  fun `allows setting callback with or without url prefix`() {
    val uri = Sep7Tx("web+stellar:tx")
    assertEquals(Sep7OperationType.TX, uri.operationType)
    assertNull(uri.callback)

    // Should remove "url:" prefix when getting
    uri.callback = "url:https://example.com/callback"
    assertEquals("https://example.com/callback", uri.callback)

    uri.callback = "https://example.com/callback"
    assertEquals("https://example.com/callback", uri.callback)

    assertEquals(
      "web+stellar:tx?callback=url%3Ahttps%3A%2F%2Fexample.com%2Fcallback",
      uri.toString()
    )
  }

  @Test
  fun `get and set msg`() {
    val uri = Sep7Tx("web+stellar:tx?msg=test%20message")
    assertEquals("test message", uri.msg)

    uri.msg = "another message"
    assertEquals("another message", uri.msg)

    uri.msg = null
    assertNull(uri.msg)
  }

  @Test
  fun `throws error when msg exceeds max length`() {
    val longMsg = "a".repeat(URI_MSG_MAX_LENGTH + 1)

    assertThrows<Sep7LongMsgError> { Sep7Tx("web+stellar:tx?msg=$longMsg") }

    val uri = Sep7Tx("web+stellar:tx")
    assertThrows<Sep7LongMsgError> { uri.msg = longMsg }
  }

  @Test
  fun `Sep7Pay forDestination creates instance with destination`() {
    val destination = testKeypair2.accountId
    val uri = Sep7Pay.forDestination(destination)

    assertEquals(destination, uri.destination)
    assertEquals(Sep7OperationType.PAY, uri.operationType)
  }

  @Test
  fun `Sep7Pay builder pattern works`() {
    val uri =
      Sep7Pay.forDestination(testKeypair1.accountId)
        .amount("100")
        .assetCode("USDC")
        .assetIssuer("GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")
        .memo("Payment for services")
        .memoType("TEXT")
        .callback("https://example.com/callback")
        .msg("Please sign this payment")

    assertEquals("100", uri.amount)
    assertEquals("USDC", uri.assetCode)
    assertEquals("GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5", uri.assetIssuer)
    assertEquals("Payment for services", uri.memo)
    assertEquals("TEXT", uri.memoType)
    assertEquals("https://example.com/callback", uri.callback)
    assertEquals("Please sign this payment", uri.msg)
  }

  @Test
  fun `Sep7Tx replacements parsing and encoding`() {
    val replacements =
      listOf(
        Sep7Replacement("X", "sourceAccount", "account from where you want to pay fees"),
        Sep7Replacement(
          "Y",
          "operations[0].sourceAccount",
          "account that needs to receive the payment"
        )
      )

    // Test encoding
    val encoded = Sep7Parser.sep7ReplacementsToString(replacements)
    assertEquals(
      "sourceAccount:X,operations[0].sourceAccount:Y;X:account from where you want to pay fees,Y:account that needs to receive the payment",
      encoded
    )

    // Test parsing
    val parsed = Sep7Parser.sep7ReplacementsFromString(encoded)
    assertEquals(2, parsed.size)
    assertEquals("X", parsed[0].id)
    assertEquals("sourceAccount", parsed[0].path)
    assertEquals("account from where you want to pay fees", parsed[0].hint)
    assertEquals("Y", parsed[1].id)
    assertEquals("operations[0].sourceAccount", parsed[1].path)
    assertEquals("account that needs to receive the payment", parsed[1].hint)
  }

  @Test
  fun `isValidSep7Uri validates tx operations correctly`() {
    // Valid tx URI
    var validTxResult = Sep7Parser.isValidSep7Uri("web+stellar:tx?xdr=$TX_XDR")
    assertTrue(validTxResult.result)
    assertNull(validTxResult.reason)

    val otherValidTx =
      "AAAAAgAAAACM6IR9GHiRoVVAO78JJNksy2fKDQNs2jBn8bacsRLcrDucQIQAAAWIAAAAMQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAGAAAAAAAAAABHkEVdJ%2BUfDnWpBr%2FqF582IEoDQ0iW0WPzO9CEUdvvh8AAAAEbWludAAAAAIAAAASAAAAAAAAAADoFl2ACT9HZkbCeuaT9MAIdStpdf58wM3P24nl738AnQAAAAoAAAAAAAAAAAAAAAAAAAAFAAAAAQAAAAAAAAAAAAAAAR5BFXSflHw51qQa%2F6hefNiBKA0NIltFj8zvQhFHb74fAAAABG1pbnQAAAACAAAAEgAAAAAAAAAA6BZdgAk%2FR2ZGwnrmk%2FTACHUraXX%2BfMDNz9uJ5e9%2FAJ0AAAAKAAAAAAAAAAAAAAAAAAAABQAAAAAAAAABAAAAAAAAAAIAAAAGAAAAAR5BFXSflHw51qQa%2F6hefNiBKA0NIltFj8zvQhFHb74fAAAAFAAAAAEAAAAHa35L%2B%2FRxV6EuJOVk78H5rCN%2BeubXBWtsKrRxeLnnpRAAAAABAAAABgAAAAEeQRV0n5R8OdakGv%2BoXnzYgSgNDSJbRY%2FM70IRR2%2B%2BHwAAABAAAAABAAAAAgAAAA8AAAAHQmFsYW5jZQAAAAASAAAAAAAAAADoFl2ACT9HZkbCeuaT9MAIdStpdf58wM3P24nl738AnQAAAAEAYpBIAAAfrAAAAJQAAAAAAAAdYwAAAAA%3D"
    validTxResult = Sep7Parser.isValidSep7Uri("web+stellar:tx?xdr=$otherValidTx")
    assertTrue(validTxResult.result)
    assertNull(validTxResult.reason)

    // Missing xdr parameter
    val missingXdrResult = Sep7Parser.isValidSep7Uri("web+stellar:tx")
    assertFalse(missingXdrResult.result)
    assertTrue(missingXdrResult.reason?.contains("must have a 'xdr' parameter") == true)

    // Invalid XDR
    val invalidXdrResult = Sep7Parser.isValidSep7Uri("web+stellar:tx?xdr=invalid")
    assertFalse(invalidXdrResult.result)
    assertTrue(invalidXdrResult.reason?.contains("not a valid transaction envelope") == true)
  }

  @Test
  fun `is not passing signature verification if no origin_domain or signature`() {
    val parsedSep7 = Wallet.Testnet.uri().parseUri("web+stellar:tx?xdr=$TX_XDR")
    assertTrue(parsedSep7 is Sep7Tx)
    // When no origin_domain and signature are present, verifySignature should return false
    var passedVerification = runBlocking { parsedSep7.verifySignature() }
    assertFalse(passedVerification)

    // Signature missing
    parsedSep7.originDomain = "place.domain.com"
    passedVerification = runBlocking { parsedSep7.verifySignature() }
    assertFalse(passedVerification)
  }

  @Test
  fun `is not passing signature verification if no toml`() {
    val parsedSep7 = Wallet.Testnet.parseSep7Uri("web+stellar:tx?xdr=$TX_XDR")
    assertTrue(parsedSep7 is Sep7Tx)
    parsedSep7.originDomain = "place.domain.com" // no stellar.toml here
    val passedVerification = runBlocking { parsedSep7.verifySignature() }
    parsedSep7.addSignature(
      KeyPair.fromSecretSeed("SBKQDF56C5VY2YQTNQFGY7HM6R3V6QKDUEDXZQUCPQOP2EBZWG2QJ2JL")
    )
    assertFalse(passedVerification)
  }

  @Test
  fun `creates signature correctly`() {
    val parsedSep7 =
      Sep7.parseUri(
        "web+stellar:pay?destination=GCALNQQBXAPZ2WIRSDDBMSTAKCUH5SG6U76YBFLQLIXJTF7FE5AX7AOO&amount=120.1234567&memo=skdjfasf&memo_type=MEMO_TEXT&msg=pay%20me%20with%20lumens&origin_domain=someDomain.com"
      )

    val signature =
      parsedSep7.addSignature(
        KeyPair.fromSecretSeed("SBKQDF56C5VY2YQTNQFGY7HM6R3V6QKDUEDXZQUCPQOP2EBZWG2QJ2JL")
      )
    val expectedSignature =
      "G/OEnKi7yT4VP2ba7pbrhStH131GQKbg8M7lJTCcGWKo80RbTvTc2Dx5BEpN23Z36gNYBc4/wbBEcu66fuR6DQ=="
    assertEquals(expectedSignature, signature)
  }

  @Test
  fun `passes signature verification`() {
    val parsedSep7 = Sep7.parseUri("web+stellar:tx?xdr=$TX_XDR")
    assertTrue(parsedSep7 is Sep7Tx)
    parsedSep7.originDomain = "place.domain.com"

    // Sign with the test key that will be validated against the mocked TOML
    // Secret key: SBA2XQ5SRUW5H3FUQARMC6QYEPUYNSVCMM4PGESGVB2UIFHLM73TPXXF
    // Public key: GDGUF4SCNINRDCRUIVOMDYGIMXOWVP3ZLMTL2OGQIWMFDDSECZSFQMQV
    parsedSep7.addSignature(
      KeyPair.fromSecretSeed("SBA2XQ5SRUW5H3FUQARMC6QYEPUYNSVCMM4PGESGVB2UIFHLM73TPXXF")
    )

    // Mock the StellarToml.getToml method to return our test TOML data
    mockkObject(StellarToml)

    try {
      val mockTomlInfo = mockk<TomlInfo>()
      // Mock the uriRequestSigningKey property to return our test signer's public key
      every { mockTomlInfo.uriRequestSigningKey } returns
        "GDGUF4SCNINRDCRUIVOMDYGIMXOWVP3ZLMTL2OGQIWMFDDSECZSFQMQV"
      // Mock the services property (required by verifySignature)
      every { mockTomlInfo.services } returns
        mockk {
          every { sep7 } returns null // No sep7 service, so it will use uriRequestSigningKey
        }

      // Mock the static getToml method to return our mock TomlInfo
      coEvery { StellarToml.getToml(any(), any()) } returns mockTomlInfo

      val passedVerification = runBlocking { parsedSep7.verifySignature() }
      assertTrue(passedVerification)
    } finally {
      // Always unmock to avoid interference with other tests
      unmockkObject(StellarToml)
    }
  }

  @Test
  fun `fails signature verification on invalid signature`() {
    val parsedSep7 = Sep7.parseUri("web+stellar:tx?xdr=$TX_XDR")
    assertTrue(parsedSep7 is Sep7Tx)
    parsedSep7.originDomain = "place.domain.com"

    // Sign with the wrong test key that will be validated against the mocked TOML
    // Wrong Secret key: SBKQDF56C5VY2YQTNQFGY7HM6R3V6QKDUEDXZQUCPQOP2EBZWG2QJ2JL
    // Public key: GDGUF4SCNINRDCRUIVOMDYGIMXOWVP3ZLMTL2OGQIWMFDDSECZSFQMQV
    parsedSep7.addSignature(
      KeyPair.fromSecretSeed("SBKQDF56C5VY2YQTNQFGY7HM6R3V6QKDUEDXZQUCPQOP2EBZWG2QJ2JL")
    )

    // Mock the StellarToml.getToml method to return our test TOML data
    mockkObject(StellarToml)

    try {
      val mockTomlInfo = mockk<TomlInfo>()
      // Mock the uriRequestSigningKey property to return our test signer's public key
      every { mockTomlInfo.uriRequestSigningKey } returns
        "GDGUF4SCNINRDCRUIVOMDYGIMXOWVP3ZLMTL2OGQIWMFDDSECZSFQMQV"
      // Mock the services property (required by verifySignature)
      every { mockTomlInfo.services } returns
        mockk {
          every { sep7 } returns null // No sep7 service, so it will use uriRequestSigningKey
        }

      // Mock the static getToml method to return our mock TomlInfo
      coEvery { StellarToml.getToml(any(), any()) } returns mockTomlInfo

      val passedVerification = runBlocking { parsedSep7.verifySignature() }
      assertFalse(passedVerification)
    } finally {
      // Always unmock to avoid interference with other tests
      unmockkObject(StellarToml)
    }
  }

  @Test
  fun `parseSep7Uri parses tx URIs correctly`() {
    val uriStr = "web+stellar:tx?xdr=$TX_XDR&pubkey=${testKeypair1.accountId}"
    val uri = Sep7Parser.parseSep7Uri(uriStr) as Sep7Tx

    assertEquals(Sep7OperationType.TX, uri.operationType)
    assertNotNull(uri.xdr)
    assertEquals(testKeypair1.accountId, uri.pubkey)
  }

  @Test
  fun `parseSep7Uri parses pay URIs correctly`() {
    val destination = testKeypair2.accountId
    val uriStr = "web+stellar:pay?destination=$destination&amount=100&asset_code=USDC"
    val uri = Sep7Parser.parseSep7Uri(uriStr) as Sep7Pay

    assertEquals(Sep7OperationType.PAY, uri.operationType)
    assertEquals(destination, uri.destination)
    assertEquals("100", uri.amount)
    assertEquals("USDC", uri.assetCode)
  }

  @Test
  fun `parseSep7Uri throws on invalid URIs`() {
    assertThrows<Sep7InvalidUriError> { Sep7Parser.parseSep7Uri("invalid:uri") }

    assertThrows<Sep7InvalidUriError> { Sep7Parser.parseSep7Uri("web+stellar:unknown") }
  }

  @Test
  fun `signature creation works correctly`() {
    // Test signature creation
    val uri = Sep7Pay.forDestination(testKeypair2.accountId)
    uri.originDomain = "example.com"
    uri.amount = "100"

    // Add signature
    val signature = uri.addSignature(testKeypair1)

    // Verify signature was added
    assertNotNull(signature)
    assertEquals(signature, uri.signature)

    // Verify the signature format (base64 encoded)
    assertTrue(signature.isNotEmpty())
    assertDoesNotThrow { Base64.getDecoder().decode(signature) }

    // Verify we can add signature with different keypair
    val newSignature = uri.addSignature(testKeypair2)
    assertNotNull(newSignature)
    assertNotEquals(
      signature,
      newSignature
    ) // Different keypairs should produce different signatures
  }

  @Test
  fun `clone creates deep copy`() {
    val original = Sep7Pay.forDestination(testKeypair1.accountId)
    original.amount = "100"
    original.assetCode = "USDC"

    val clone = original.clone()

    // Verify clone has same values
    assertEquals(original.destination, clone.destination)
    assertEquals(original.amount, clone.amount)
    assertEquals(original.assetCode, clone.assetCode)

    // Modify clone shouldn't affect original
    clone.amount = "200"
    assertEquals("100", original.amount)
    assertEquals("200", clone.amount)
  }

  @Test
  fun `Sep7Tx forTransaction creates correct instance`() {
    // Create a mock transaction
    val mockTransaction = mockk<Transaction>()
    val mockEnvelope = mockk<TransactionEnvelope>()
    val xdrBytes = "test".toByteArray()

    every { mockTransaction.toEnvelopeXdr() } returns mockEnvelope
    every { mockTransaction.network } returns Network.TESTNET
    every { mockEnvelope.toXdrByteArray() } returns xdrBytes

    val uri = Sep7Tx.forTransaction(mockTransaction)

    assertEquals(Base64.getEncoder().encodeToString(xdrBytes), uri.xdr)
    assertEquals(Network.TESTNET, uri.networkPassphrase)
  }

  @Test
  fun `empty replacements handling`() {
    // Empty string should return empty list
    val emptyList = Sep7Parser.sep7ReplacementsFromString("")
    assertTrue(emptyList.isEmpty())

    // Null should return empty list
    val nullList = Sep7Parser.sep7ReplacementsFromString(null)
    assertTrue(nullList.isEmpty())

    // Empty list should return empty string
    val emptyString = Sep7Parser.sep7ReplacementsToString(emptyList())
    assertEquals("", emptyString)

    // Null should return empty string
    val nullString = Sep7Parser.sep7ReplacementsToString(null)
    assertEquals("", nullString)
  }

  @Test
  fun `replacements without hints`() {
    val replacements =
      listOf(
        Sep7Replacement("X", "sourceAccount", ""),
        Sep7Replacement("Y", "operations[0].sourceAccount", "")
      )

    val encoded = Sep7Parser.sep7ReplacementsToString(replacements)
    assertEquals("sourceAccount:X,operations[0].sourceAccount:Y", encoded)

    val parsed = Sep7Parser.sep7ReplacementsFromString(encoded)
    assertEquals(2, parsed.size)
    assertEquals("", parsed[0].hint)
    assertEquals("", parsed[1].hint)
  }

  @Test
  fun `replacements with same hints`() {
    val first = Sep7Replacement("X", "sourceAccount", "account from where you want to pay fees")

    // second an third have the same hint for Y
    val second =
      Sep7Replacement(
        "Y",
        "operations[0].sourceAccount",
        "account that needs the trustline and which will receive the new tokens"
      )
    val third =
      Sep7Replacement(
        "Y",
        "operations[1].destination",
        "account that needs the trustline and which will receive the new tokens"
      )

    var replacements = listOf(first, second, third)

    val encoded = Sep7Parser.sep7ReplacementsToString(replacements)
    // for Y only one hint
    val expected =
      "sourceAccount:X,operations[0].sourceAccount:Y,operations[1].destination:Y;X:account from where you want to pay fees,Y:account that needs the trustline and which will receive the new tokens"
    assertEquals(expected, encoded)

    val sep7Tx = Sep7Tx()
    sep7Tx.xdr = TX_XDR
    sep7Tx.setReplacements(listOf(first, second))
    sep7Tx.addReplacement(third)
    val validationResult = Sep7.isValidUri(sep7Tx.toString())
    assertTrue(validationResult.result)

    replacements = Sep7Parser.sep7ReplacementsFromString(encoded)
    assertEquals(3, replacements.size)
    assertEquals(first.id, replacements[0].id)
    assertEquals(first.path, replacements[0].path)
    assertEquals(first.hint, replacements[0].hint)
    assertEquals(second.id, replacements[1].id)
    assertEquals(second.path, replacements[1].path)
    assertEquals(second.hint, replacements[1].hint)
    assertEquals(third.id, replacements[2].id)
    assertEquals(third.path, replacements[2].path)
    assertEquals(third.hint, replacements[2].hint)

    sep7Tx.getReplacements()
    assertEquals(3, replacements.size)
    assertEquals(first.id, replacements[0].id)
    assertEquals(first.path, replacements[0].path)
    assertEquals(first.hint, replacements[0].hint)
    assertEquals(second.id, replacements[1].id)
    assertEquals(second.path, replacements[1].path)
    assertEquals(second.hint, replacements[1].hint)
    assertEquals(third.id, replacements[2].id)
    assertEquals(third.path, replacements[2].path)
    assertEquals(third.hint, replacements[2].hint)
  }

  @Test
  fun `URI with special characters in msg`() {
    val specialMsg = "Hello & goodbye < > \" ' % @ #"
    val uri = Sep7Pay()
    uri.msg = specialMsg

    assertEquals(specialMsg, uri.msg)

    // Parse the URI string back
    val uriString = uri.toString()
    val parsed = Sep7Pay(uriString)
    assertEquals(specialMsg, parsed.msg)
  }

  @Test
  fun `network passphrase handling`() {
    // Default to PUBLIC
    val uri1 = Sep7Tx()
    assertEquals(Network.PUBLIC, uri1.networkPassphrase)

    // Parse from URI with testnet
    val uri2 =
      Sep7Tx(
        "web+stellar:tx?network_passphrase=${Network.TESTNET.networkPassphrase.replace(" ", "%20")}"
      )
    assertEquals(Network.TESTNET, uri2.networkPassphrase)

    // Set custom network
    val customNetwork = Network("Custom Network ; 2024")
    uri1.networkPassphrase = customNetwork
    assertEquals(customNetwork.networkPassphrase, uri1.networkPassphrase.networkPassphrase)
  }

  @Test
  fun `doc test`() {
    // test for doc at https://developers.stellar.org/docs/build/apps/wallet/sep7
    val wallet = Wallet.Testnet
    val stellar = wallet.stellar()

    // chapter Tx Operation
    val sourceAccountId = "GBUM4IKFYUFQIHRVPAVRODSDQFZC4FQHLOFT767G7D5PJMAHLADKIPDM"
    friendbotHelper(sourceAccountId)
    val sourceAccount = sourceAccountId.toPublicKeyPair()
    val destinationAccountId = "GCXQFRBDBMYJRYPBXCO73AMP37ZHQU52LJ7JPP27T6O2QGDWZAJFA6RL"
    val destinationAccount = destinationAccountId.toPublicKeyPair()

    val txBuilder = runBlocking { stellar.transaction(sourceAccount) }
    val tx = txBuilder.createAccount(destinationAccount).build()
    val xdr = URLEncoder.encode(tx.toEnvelopeXdrBase64(), "UTF-8").replace("+", "%20")
    val callback = URLEncoder.encode("https://example.com/callback", "UTF-8")
    val txUri = "web+stellar:tx?xdr=${xdr}&callback=${callback}"
    var uri = wallet.parseSep7Uri(txUri)
    assertTrue(uri is Sep7Tx)

    uri = Sep7Tx(txUri)
    uri.addReplacement(
      Sep7Replacement("X", "sourceAccount", "account from where you want to pay fees")
    )
    assertEquals(1, uri.getReplacements().size)

    uri = Sep7Tx.forTransaction(tx)
    uri.callback = "https://example.com/callback"
    uri.msg = "here goes a message"
    assertNotNull(uri.callback)
    assertNotNull(uri.msg)
    // print(uri.toString())

    // chapter Pay Operation
    val destination = "GBSJOF7QCVSYJZSF6QTPWM2LHID6SB63NTLC4BLYI3YNUC3U4YAYRWRC"
    val assetIssuer = "GA6CPK3EWYIGZIRAIMO2M4UAVHB5Q7H7WGG242BGLM2NFSDYMZL2MXRJ"
    val assetCode = "USDC"
    val amount = "120.1234567"
    val memo = "memo"
    val message = URLEncoder.encode("pay me with lumens", "UTF-8").replace("+", "%20")
    val originDomain = "example.com"
    val payUri =
      "web+stellar:pay?destination=${destination}&amount=${amount}&memo=${memo}&msg=${message}&origin_domain=${originDomain}&asset_issuer=${assetIssuer}&asset_code=${assetCode}"
    uri = wallet.parseSep7Uri(payUri)
    assertTrue(uri is Sep7Pay)

    uri = Sep7Pay.forDestination("GBSJOF7QCVSYJZSF6QTPWM2LHID6SB63NTLC4BLYI3YNUC3U4YAYRWRC")
    uri.callback = "https://example.com/callback"
    uri.msg = "here goes a message"
    uri.assetCode = "USDC"
    uri.assetIssuer = "GA6CPK3EWYIGZIRAIMO2M4UAVHB5Q7H7WGG242BGLM2NFSDYMZL2MXRJ"
    uri.amount = "10"
    assertNotNull(uri.callback)
    assertNotNull(uri.msg)
    assertNotNull(uri.assetCode)
    assertNotNull(uri.assetIssuer)
    assertNotNull(uri.amount)
    // print(uri.toString())

    uri = Sep7Pay.forDestination("GAVKOJCRSY5AUGSHVLUHLHX6ERBF57SRQKUOJ2JILBE3TQNIBMA3ODI6")
    uri.originDomain = "example.com"
    val keyPair = wallet.stellar().account().createKeyPair()
    uri.addSignature(KeyPair.fromSecretSeed(keyPair.secretKey))
    assertNotNull(uri.signature)
    // print(uri.signature)
  }

  fun friendbotHelper(address: String) {
    try {
      val friendbotUrl = java.lang.String.format("https://friendbot.stellar.org/?addr=%s", address)
      URL(friendbotUrl).openStream()
    } catch (e: Exception) {
      // already funded
    }
  }
}
