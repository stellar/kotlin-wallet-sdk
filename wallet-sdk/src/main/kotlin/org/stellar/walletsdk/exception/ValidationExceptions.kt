package org.stellar.walletsdk.exception

open class ValidationException : WalletException {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

object ClientDomainWithMemoException :
  ValidationException("Client domain cannot be used with memo")

class InvalidAnchorServiceUrl(e: Exception) :
  ValidationException("Anchor service URL is invalid", e)

object InvalidMemoIdException : ValidationException("Memo ID must be a positive integer")

object InvalidStartingBalanceException :
  ValidationException("Starting balance must be at least 1 XLM for non-sponsored accounts")

object InvalidSponsoredAccountException :
  ValidationException(
    "No other operations are allowed with create account operation per sponsoring block"
  )

// Invalid response from server
sealed class InvalidResponseException(message: String) : WalletException(message)

object MissingTokenException : InvalidResponseException("Token was not returned")

object MissingTransactionException :
  InvalidResponseException("The response did not contain a transaction")

object NetworkMismatchException : InvalidResponseException("Networks don't match")

class InvalidDataException(message: String) : InvalidResponseException(message)

class InvalidJsonException(reason: String, json: Any) :
  InvalidResponseException("Invalid json response object: $reason. Json: $json")

class InvalidChallengeException(message: String) :
  ValidationException("Invalid SEP-10 challenge: $message")

class ChallengeClientAccountMismatchException(expected: String, got: String) :
  ValidationException("SEP-10 challenge client account mismatch: expected $expected, got $got")

class DomainSigningModifiedException :
  ValidationException("Domain signing server returned a modified transaction")
