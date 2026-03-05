package org.stellar.walletsdk.uri

import java.net.URI

/**
 * The Sep-7 'pay' operation represents a request to pay a specific address with a specific asset,
 * regardless of the source asset used by the payer.
 *
 * @see [SEP-0007 Pay
 * Operation](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0007.md#operation-pay)
 */
class Sep7Pay : Sep7Base {

  /**
   * Creates a new instance of the Sep7Pay class.
   *
   * @param uri URI to initialize the Sep7Pay instance (URL or string)
   */
  constructor(uri: String = "$WEB_STELLAR_SCHEME${Sep7OperationType.PAY}") : super(uri)

  /**
   * Creates a new instance of the Sep7Pay class from a URI object.
   *
   * @param uri URI object to initialize the Sep7Pay instance
   */
  constructor(uri: URI) : super(uri)

  /**
   * Returns a deep clone of this instance.
   *
   * @return a deep clone of this Sep7Pay instance
   */
  override fun clone(): Sep7Pay = Sep7Pay(this.uri)

  /** Gets/sets the destination of the payment request, which should be a valid Stellar address. */
  var destination: String?
    get() = getParam("destination")
    set(value) {
      setParam("destination", value)
    }

  /** Gets/sets the amount that destination should receive. */
  var amount: String?
    get() = getParam("amount")
    set(value) {
      setParam("amount", value)
    }

  /** Gets/sets the code from the asset that destination should receive. */
  var assetCode: String?
    get() = getParam("asset_code")
    set(value) {
      setParam("asset_code", value)
    }

  /** Gets/sets the account ID of asset issuer the destination should receive. */
  var assetIssuer: String?
    get() = getParam("asset_issuer")
    set(value) {
      setParam("asset_issuer", value)
    }

  /**
   * Gets/sets the memo to be included in the payment / path payment. Memos of type MEMO_HASH and
   * MEMO_RETURN should be base64-decoded after returned from the getter and base64-encoded before
   * passed to the setter.
   */
  var memo: String?
    get() = getParam("memo")
    set(value) {
      setParam("memo", value)
    }

  /** Gets/sets the type of the memo. Supported values: "TEXT", "ID", "HASH", "RETURN" */
  var memoType: String?
    get() = getParam("memo_type")
    set(value) {
      setParam("memo_type", value)
    }

  companion object {
    /**
     * Creates a Sep7Pay instance with given destination.
     *
     * @param destination a valid Stellar address to receive the payment
     * @return the Sep7Pay instance
     */
    @JvmStatic
    fun forDestination(destination: String): Sep7Pay {
      val uri = Sep7Pay()
      uri.destination = destination
      return uri
    }
  }

  /** Builder pattern support for fluent API */
  fun amount(amount: String): Sep7Pay {
    this.amount = amount
    return this
  }

  fun assetCode(code: String): Sep7Pay {
    this.assetCode = code
    return this
  }

  fun assetIssuer(issuer: String): Sep7Pay {
    this.assetIssuer = issuer
    return this
  }

  fun memo(memo: String): Sep7Pay {
    this.memo = memo
    return this
  }

  fun memoType(type: String): Sep7Pay {
    this.memoType = type
    return this
  }

  fun callback(callback: String): Sep7Pay {
    this.callback = callback
    return this
  }

  fun msg(msg: String): Sep7Pay {
    this.msg = msg
    return this
  }
}
