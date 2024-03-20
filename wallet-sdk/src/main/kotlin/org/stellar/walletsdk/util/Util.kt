package org.stellar.walletsdk.util

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Security
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Duration
import java.time.Instant
import kotlinx.serialization.SerializationException
import org.bouncycastle.asn1.DEROctetString
import org.bouncycastle.asn1.edec.EdECObjectIdentifiers
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.stellar.sdk.StrKey
import org.stellar.sdk.TimeBounds
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.asset.*
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.json.fromJson
import org.stellar.walletsdk.toml.TomlInfo

internal object Util {
  internal fun String.isHex(): Boolean {
    return this.toBigIntegerOrNull(16) != null
  }

  internal suspend inline fun <reified T> HttpClient.anchorGet(
    info: TomlInfo,
    authToken: AuthToken? = null,
    urlBlock: URLBuilder.() -> Unit = {},
  ): T {
    val urlBuilder =
      URLBuilder(
        info.services.sep24?.transferServerSep24 ?: throw AnchorInteractiveFlowNotSupported
      )
    urlBuilder.urlBlock()

    return this.authGet(urlBuilder.build().toString(), authToken)
  }

  internal suspend inline fun <reified T> HttpClient.authGet(
    url: String,
    authToken: AuthToken? = null,
  ): T {
    return this.authGetStringToken(url, authToken.toString())
  }

  internal suspend inline fun <reified T> HttpClient.authGetStringToken(
    url: String,
    authToken: String? = null,
  ): T {
    val textBody =
      this.get(url) {
          if (authToken != null) {
            headers { append(HttpHeaders.Authorization, "Bearer $authToken") }
          }
        }
        .bodyAsText()

    return textBody.fromJsonOrError()
  }

  internal suspend inline fun <reified Req, reified Resp> HttpClient.postJson(
    url: String,
    requestBody: Req,
    authToken: AuthToken? = null,
    block: HttpRequestBuilder.() -> Unit = {}
  ): Resp {
    val result =
      this.post(url) {
          contentType(ContentType.Application.Json)
          setBody(requestBody)
          if (authToken != null) {
            headers { append(HttpHeaders.Authorization, "Bearer $authToken") }
          }
          block()
        }
        .bodyAsText()

    return result.fromJsonOrError()
  }

  internal suspend inline fun <reified Req, reified Resp> HttpClient.putJson(
    url: String,
    requestBody: Req,
    authToken: AuthToken? = null,
    block: HttpRequestBuilder.() -> Unit = {}
  ): Resp {
    val result =
      this.put(url) {
          contentType(ContentType.Application.Json)
          setBody(requestBody)
          if (authToken != null) {
            headers { append(HttpHeaders.Authorization, "Bearer $authToken") }
          }
          block()
        }
        .bodyAsText()

    return result.fromJsonOrError()
  }

  internal suspend inline fun HttpClient.authDelete(
    url: String,
    memo: String?,
    authToken: AuthToken?,
  ) {
    this.delete(url) {
      if (authToken != null) {
        headers { append(HttpHeaders.Authorization, "Bearer $authToken") }
      }
      if (memo != null) {
        contentType(ContentType.Application.Json)
        setBody(mapOf("memo" to memo))
      }
    }
  }

  private inline fun <reified T> String.fromJsonOrError(): T {
    return try {
      this.fromJson()
    } catch (e: SerializationException) {
      try {
        val error = this.fromJson<AnchorErrorResponse>()
        throw AnchorRequestException(error.error, e)
      } catch (ignored: SerializationException) {}

      throw AnchorRequestException("Failed to deserialize string: $this", e)
    }
  }

  internal fun SigningKeyPair.toJava(): java.security.KeyPair {
    Security.addProvider(BouncyCastleProvider())

    val factory: KeyFactory = KeyFactory.getInstance("Ed25519")
    val privKeyInfo =
      PrivateKeyInfo(
        AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519),
        DEROctetString(StrKey.decodeEd25519SecretSeed(this.keyPair.secretSeed))
      )
    val pkcs8KeySpec = PKCS8EncodedKeySpec(privKeyInfo.getEncoded())

    val pubKeyInfo =
      SubjectPublicKeyInfo(AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519), this.publicKey)
    val x509KeySpec = X509EncodedKeySpec(pubKeyInfo.getEncoded())
    var jcaPublicKey = factory.generatePublic(x509KeySpec)

    val private: PrivateKey = factory.generatePrivate(pkcs8KeySpec)

    return java.security.KeyPair(jcaPublicKey, private)
  }
}

fun Duration.toTimeBounds(): TimeBounds {
  return TimeBounds(0, Instant.now().plus(this).toEpochMilli() / 1000)
}

fun AnchorTransaction.requireStatus(requiredStatus: TransactionStatus) {
  if (this.status != requiredStatus) {
    throw IncorrectTransactionStatusException(this, requiredStatus)
  }
}

fun String.toAssetId(): AssetId {
  val str = this
  if (str == NativeAssetId.id) {
    return NativeAssetId
  } else if (str.startsWith(STELLAR_SCHEME)) {
    val split = str.split(":")

    // scheme:code:issuer
    if (split.size != 3) {
      throw InvalidJsonException("Invalid asset format", str)
    }

    return IssuedAssetId(split[1], split[2])
  } else if (str.startsWith(FIAT_SCHEME)) {
    return FiatAssetId(str)
  }
  throw InvalidJsonException("Unknown scheme", str)
}
