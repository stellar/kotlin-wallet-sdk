package org.stellar.walletsdk.anchor

import io.ktor.client.*
import io.ktor.http.*
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.exception.*

/** Build on/off ramps with anchors. */
expect class Anchor internal constructor(cfg: Config, baseUrl: Url, httpClient: HttpClient)
