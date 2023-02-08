package org.stellar.walletsdk.anchor

import org.stellar.walletsdk.Config
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.toml.TomlInfo

/** Build on/off ramps with anchors. */
expect class Anchor internal constructor(
  cfg: Config,
  homeDomain: String,
) {
  internal val cfg: Config
  internal val homeDomain: String



}