package org.stellar.walletsdk.recovery

import org.stellar.walletsdk.AccountSigner
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.horizon.Transaction

actual class Recovery internal actual constructor(
    cfg: Config,
    stellar: Stellar
) {

    // TODO: create account helper to handle 409 Conflict > fetch account data from RS and return
    //  signers[0].key
    // TODO: handle update RS account info (PUT request)
    /**
     * Sign transaction with recovery servers. It is used to recover an account using
     * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
     *
     * @param transaction Transaction with new signer to be signed by recovery servers
     * @param accountAddress Stellar address of the account that is recovered
     * @param recoveryServers List of recovery servers to use
     * @return transaction with recovery server signatures
     * @throws [ServerRequestFailedException] when request fails
     * @throws [NotAllSignaturesFetchedException] when all recovery servers don't return signatures
     */
    actual suspend fun signWithRecoveryServers(
        transaction: Transaction,
        accountAddress: String,
        recoveryServers: List<RecoveryServerAuth>
    ): Transaction {
        TODO("Not yet implemented")
    }

    /**
     * Register account with recovery servers using
     * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
     *
     * @param recoveryServers A list of recovery servers to register with
     * @param accountIdentity A list of account identities to be registered with the recovery servers
     * @return a list of recovery servers' signatures
     * @throws [ServerRequestFailedException] when request fails
     * @throws [RecoveryException] when error happens working with recovery servers
     */
    actual suspend fun enrollWithRecoveryServer(
        recoveryServers: List<RecoveryServer>,
        account: AccountKeyPair,
        accountIdentity: List<RecoveryAccountIdentity>
    ): List<String> {
        TODO("Not yet implemented")
    }

    /**
     * Create new recoverable wallet using
     * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md). It
     * registers the account with recovery servers, adds recovery servers and device account as new
     * account signers, and sets threshold weights on the account.
     *
     * This transaction can be sponsored.
     *
     * Uses [enrollWithRecoveryServer] and [registerRecoveryServerSigners] internally.
     *
     * @param config: RecoverableWalletConfig
     * @return transaction
     * @throws [ServerRequestFailedException] when request fails
     * @throws [RecoveryException] when error happens working with recovery servers
     * @throws [HorizonRequestFailedException] for Horizon exceptions
     */
    actual suspend fun createRecoverableWallet(config: RecoverableWalletConfig): Transaction {
        TODO("Not yet implemented")
    }

    /**
     * Add recovery servers and device account as new account signers, and set new threshold weights
     * on the account.
     *
     * This transaction can be sponsored.
     *
     * @param accountSigner A list of account signers and their weights
     * @param accountThreshold Low, medium, and high thresholds to set on the account
     * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
     * @return transaction
     * @throws [HorizonRequestFailedException] for Horizon exceptions
     */
    actual suspend fun registerRecoveryServerSigners(
        account: AccountKeyPair,
        accountSigner: List<AccountSigner>,
        accountThreshold: AccountThreshold,
        sponsorAddress: String?
    ): Transaction {
        TODO("Not yet implemented")
    }

}