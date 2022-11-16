package org.stellar.walletsdk.util

import java.io.IOException
import kotlinx.coroutines.*
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.AccountNotFoundException

/**
 * Fetch account information from the Stellar network.
 *
 * @param accountAddress Stellar address of the account
 * @param server Horizon [Server] instance
 *
 * @return Account response object
 *
 * @throws [AccountNotFoundException] when account is not found
 */
suspend fun fetchAccount(accountAddress: String, server: Server): AccountResponse = coroutineScope {
  try {
    server.accounts().account(accountAddress)
  } catch (e: IOException) {
    throw AccountNotFoundException(accountAddress)
  }
}
