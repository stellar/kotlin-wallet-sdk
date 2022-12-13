package org.stellar.walletsdk.toml

import shadow.com.google.gson.Gson
import shadow.com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(toml: Map<*, *>?): T =
  fromJson(toJson(toml), object : TypeToken<T>() {}.type)
