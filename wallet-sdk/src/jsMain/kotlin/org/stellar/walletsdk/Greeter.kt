package org.stellar.walletsdk

@JsExport
class Greeter {
  fun greet(): String {
    return "hello world"
  }

  companion object {
    init {
      Greeter::class.js.asDynamic().test = Greeter.Companion::test
    }
    fun test() {}
  }
}

@JsExport fun Greeter.bar() {}
