package org.stellar.walletsdk.exception

sealed class RecoveryException(message: String) : WalletException(message)

object NoAccountSignersException :
    RecoveryException("There are no signers on this recovery server")

object NotAllSignaturesFetchedException : RecoveryException("Didn't get all recovery server signatures")

object NotRegisteredWithAllException : RecoveryException("Could not register with all recovery servers")
