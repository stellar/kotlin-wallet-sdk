@file:Suppress("MatchingDeclarationName")

package org.stellar.walletsdk.json

import kotlinx.serialization.KSerializer
import org.stellar.walletsdk.horizon.PublicKeyPair

expect object AccountAsStringSerializer : KSerializer<PublicKeyPair>
