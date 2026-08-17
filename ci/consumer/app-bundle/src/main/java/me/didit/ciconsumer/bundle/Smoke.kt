package me.didit.ciconsumer.bundle

import me.didit.sdk.DiditSdk

// DiditSdk (didit-sdk-core) is the bundle's only public API surface - the
// autodetection/nfc/wallet modules are internal-only and loaded reflectively
// by core, so they cannot be referenced at compile time. Their presence on
// the classpath is asserted by the verifyBundleEdges task in this module's
// build script, and D8 dexes every AAR's classes.jar during assembleDebug.
@Suppress("unused")
fun smokeEntryPoint(): String = DiditSdk::class.java.name
