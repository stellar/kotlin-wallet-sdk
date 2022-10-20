package org.stellar.walletsdk.util

import java.util.Base64
import org.stellar.sdk.KeyPair
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.sdk.xdr.Signature

/**
 * Create decorated signature using account's Stellar address (public key) and signature string.
 *
 * @param publicKey Stellar address of the account to use in the signature
 * @param signatureBase64String signature string
 * @param base64Decoder optional base64Decoder. Default `java.util.Base64` decoder works with
 * Android API 23+. To support Android API older than API 23, custom base64Decoder needs to be
 * provided. For example, `android.util.Base64`.
 *
 * @return decorated signature
 */
fun createDecoratedSignature(
  publicKey: String,
  signatureBase64String: String,
  base64Decoder: ((String) -> ByteArray)? = null
): DecoratedSignature {
  val signature = Signature()

  if (base64Decoder == null) {
    signature.signature = Base64.getDecoder().decode(signatureBase64String)
  } else {
    signature.signature = base64Decoder(signatureBase64String)
  }

  val decoratedSig = DecoratedSignature()
  decoratedSig.signature = signature
  decoratedSig.hint = KeyPair.fromAccountId(publicKey).signatureHint

  return decoratedSig
}
