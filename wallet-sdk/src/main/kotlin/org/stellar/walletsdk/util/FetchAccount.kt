package org.stellar.walletsdk.util

import java.io.IOException
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.exception.AccountNotFoundException

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
@Throws(AccountNotFoundException::class)
suspend fun fetchAccount(accountAddress: String, server: Server): AccountResponse {
  try {
    return server.accounts().account(accountAddress)
  } catch (e: IOException) {
    // TODO: check that error code is 404
    throw AccountNotFoundException(accountAddress)
  }
}
