@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external

import external.xdr.Asset
import kotlin.js.*
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

external open class AccountCallBuilder(serverUrl: URI) :
  CallBuilder<CollectionPage<AccountRecord>> {
  open fun accountId(id: String): CallBuilder<AccountRecord>
  open fun forSigner(id: String): AccountCallBuilder /* this */
  open fun forAsset(asset: Asset): AccountCallBuilder /* this */
  open fun sponsor(id: String): AccountCallBuilder /* this */
  open fun forLiquidityPool(id: String): AccountCallBuilder /* this */
}
