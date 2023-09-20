package org.stellar.walletsdk

import kotlinx.serialization.Serializable
import org.stellar.sdk.requests.RequestBuilder

/**
 * Account weights threshold
 *
 * @param low Low threshold weight
 * @param medium Medium threshold weight
 * @param high High threshold weight
 */
data class AccountThreshold(val low: Int, val medium: Int, val high: Int)

@Serializable
data class InteractiveFlowResponse(
  val id: String,
  val url: String,
  val type: String,
)

enum class Order(internal val builderEnum: RequestBuilder.Order) {
  ASC(RequestBuilder.Order.ASC),
  DESC(RequestBuilder.Order.DESC)
}
