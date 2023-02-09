package org.stellar.walletsdk.anchor

import org.stellar.walletsdk.Config
import org.stellar.walletsdk.exception.*

/** Build on/off ramps with anchors. */
expect class Anchor
internal constructor(
  cfg: Config,
  homeDomain: String,
) {
  internal val cfg: Config
  internal val homeDomain: String
}
