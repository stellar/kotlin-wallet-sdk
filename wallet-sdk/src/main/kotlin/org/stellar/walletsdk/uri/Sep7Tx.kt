package org.stellar.walletsdk.uri

import java.net.URI
import java.util.Base64
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.sdk.xdr.TransactionEnvelope

/**
 * The Sep-7 'tx' operation represents a request to sign a specific XDR TransactionEnvelope.
 *
 * @see [SEP-0007 Tx
 * Operation](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0007.md#operation-tx)
 */
class Sep7Tx : Sep7Base {

  /**
   * Creates a new instance of the Sep7Tx class.
   *
   * @param uri URI to initialize the Sep7Tx instance (URL or string)
   */
  constructor(uri: String = "$WEB_STELLAR_SCHEME${Sep7OperationType.TX}") : super(uri)

  /**
   * Creates a new instance of the Sep7Tx class from a URI object.
   *
   * @param uri URI object to initialize the Sep7Tx instance
   */
  constructor(uri: URI) : super(uri)

  /**
   * Returns a deep clone of this instance.
   *
   * @return a deep clone of this Sep7Tx instance
   */
  override fun clone(): Sep7Tx = Sep7Tx(this.uri)

  /**
   * Gets/sets the uri 'xdr' param - a Stellar TransactionEnvelope in XDR format that is base64
   * encoded and then URL-encoded.
   */
  var xdr: String?
    get() = getParam("xdr")
    set(value) {
      setParam("xdr", value)
    }

  /**
   * Gets/sets the uri 'pubkey' param. This param specifies which public key the URI handler should
   * sign for.
   */
  var pubkey: String?
    get() = getParam("pubkey")
    set(value) {
      setParam("pubkey", value)
    }

  /**
   * Gets/sets the uri 'chain' param.
   *
   * There can be an optional chain query param to include a single SEP-0007 request that spawned or
   * triggered the creation of this SEP-0007 request. This will be a URL-encoded value. The goal of
   * this field is to be informational only and can be used to forward SEP-0007 requests.
   */
  var chain: String?
    get() = getParam("chain")
    set(value) {
      setParam("chain", value)
    }

  /**
   * Gets a list of fields in the transaction that need to be replaced.
   *
   * @return list of fields that need to be replaced
   */
  fun getReplacements(): List<Sep7Replacement> {
    return Sep7Parser.sep7ReplacementsFromString(getParam("replace"))
  }

  /**
   * Sets and URL-encodes the uri 'replace' param, which is a list of fields in the transaction that
   * needs to be replaced.
   *
   * Deletes the uri 'replace' param if set as empty list or null.
   *
   * This 'replace' param should be a URL-encoded value that identifies the fields to be replaced in
   * the XDR using the 'Txrep (SEP-0011)' representation.
   *
   * @param replacements a list of replacements to set
   */
  fun setReplacements(replacements: List<Sep7Replacement>?) {
    if (replacements.isNullOrEmpty()) {
      setParam("replace", null)
    } else {
      setParam("replace", Sep7Parser.sep7ReplacementsToString(replacements))
    }
  }

  /**
   * Adds an additional replacement.
   *
   * @param replacement the replacement to add
   */
  fun addReplacement(replacement: Sep7Replacement) {
    val replacements = getReplacements().toMutableList()
    replacements.add(replacement)
    setReplacements(replacements)
  }

  /**
   * Removes all replacements with the given identifier.
   *
   * @param id the identifier to remove
   */
  fun removeReplacement(id: String) {
    val replacements = getReplacements().filter { it.id != id }
    setReplacements(replacements)
  }

  /**
   * Creates a Stellar Transaction from the URI's XDR and networkPassphrase.
   *
   * @return the Stellar Transaction
   * @throws IllegalStateException if XDR is not set
   */
  fun getTransaction(): Transaction {
    val xdrString = xdr ?: throw IllegalStateException("XDR is not set")
    val envelope = TransactionEnvelope.fromXdrBase64(xdrString)
    return Transaction.fromEnvelopeXdr(envelope, networkPassphrase) as Transaction
  }

  companion object {
    /**
     * Creates a Sep7Tx instance with given transaction.
     *
     * Sets the 'xdr' param as a Stellar TransactionEnvelope in XDR format that is base64 encoded
     * and then URL-encoded.
     *
     * @param transaction a transaction which will be used to set the URI 'xdr' and
     * 'network_passphrase' query params
     * @return the Sep7Tx instance
     */
    @JvmStatic
    fun forTransaction(transaction: Transaction): Sep7Tx {
      val uri = Sep7Tx()
      val envelope = transaction.toEnvelopeXdr()
      uri.xdr = Base64.getEncoder().encodeToString(envelope.toXdrByteArray())
      uri.networkPassphrase = transaction.network
      return uri
    }
  }

  /** Builder pattern support for fluent API */
  fun xdr(xdr: String): Sep7Tx {
    this.xdr = xdr
    return this
  }

  fun pubkey(pubkey: String): Sep7Tx {
    this.pubkey = pubkey
    return this
  }

  fun chain(chain: String): Sep7Tx {
    this.chain = chain
    return this
  }

  fun replacements(replacements: List<Sep7Replacement>): Sep7Tx {
    setReplacements(replacements)
    return this
  }

  fun callback(callback: String): Sep7Tx {
    this.callback = callback
    return this
  }

  fun msg(msg: String): Sep7Tx {
    this.msg = msg
    return this
  }

  fun networkPassphrase(network: Network): Sep7Tx {
    this.networkPassphrase = network
    return this
  }
}
