package org.stellar.walletsdk.exception

sealed class CustomerExceptions(message: String) : WalletException(message)

class CustomerUpdateException : CustomerExceptions("At least one SEP9 field should be updated")

class UnauthorizedCustomerDeletionException(account: String) :
  CustomerExceptions("Unauthorized to delete customer account $account")

class CustomerNotFoundException(account: String) :
  CustomerExceptions("Customer not found for account $account")

class ErrorOnDeletingCustomerException(account: String) :
  CustomerExceptions("Error on deleting customer for account $account")

class KYCServerNotFoundException : CustomerExceptions("Required KYC server URL not found")
