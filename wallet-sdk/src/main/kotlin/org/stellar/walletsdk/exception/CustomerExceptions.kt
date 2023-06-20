package org.stellar.walletsdk.exception

sealed class CustomerExceptions(message: String) : WalletException(message)

class UnauthorizedCustomerDeletionException(account: String) :
  CustomerExceptions("Unauthorized to delete customer account $account")

class CustomerNotFoundException(account: String) :
  CustomerExceptions("Customer not found for account $account")
