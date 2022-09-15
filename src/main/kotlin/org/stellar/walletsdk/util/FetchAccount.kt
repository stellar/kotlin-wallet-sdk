package org.stellar.walletsdk.util

import java.io.IOException
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse

fun fetchAccount(accountAddress: String, server: Server): AccountResponse {
  try {
    return server.accounts().account(accountAddress)
  } catch (e: IOException) {
    throw Exception("Account $accountAddress was not found")
  }
}
