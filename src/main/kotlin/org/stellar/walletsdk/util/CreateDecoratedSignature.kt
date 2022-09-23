package org.stellar.walletsdk.util

import java.util.Base64
import org.stellar.sdk.KeyPair
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.sdk.xdr.Signature

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
