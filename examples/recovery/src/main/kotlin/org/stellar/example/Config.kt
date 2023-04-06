package org.stellar.example

import org.stellar.walletsdk.recovery.RecoveryServer

data class Config(val recovery: Recovery, val key: String)

data class Recovery(val server1: RecoveryServer, val server2: RecoveryServer)
