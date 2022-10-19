package org.stellar.walletsdk.util

import java.io.IOException
import kotlinx.coroutines.*
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.AccountNotFoundException

suspend fun fetchAccount(accountAddress: String, server: Server): AccountResponse {
  try {
    return CoroutineScope(Dispatchers.IO)
      .async {
        return@async server.accounts().account(accountAddress)
      }
      .await()
  } catch (e: IOException) {
    throw AccountNotFoundException(accountAddress)
  }
}
