@file:JsQualifier("xdrHidden")
@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external.xdr.hidden

import external.Buffer
import external.xdr.MuxedAccount
import external.xdr.OperationBody
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

external interface `T$105` {
  var sourceAccount: MuxedAccount?
  var body: OperationBody
}

external open class Operation2<T : Operation>(attributes: `T$105`) {
  open fun sourceAccount(value: MuxedAccount? = definedExternally): MuxedAccount?
  open fun body(value: OperationBody = definedExternally): OperationBody
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Operation2__0
    fun write(value: Operation2__0, io: Buffer)
    fun isValid(value: Operation2__0): Boolean
    fun toXDR(value: Operation2__0): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Operation2__0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Operation2__0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class Operation2__0 : Operation2<Operation>
