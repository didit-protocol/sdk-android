package me.didit.ciconsumer.bundle

import me.didit.sdk.DiditSdk

// DiditSdk lives in didit-sdk-core, which this module only receives through
// the didit-sdk bundle POM's transitive edge - so this compile-time reference
// fails if the bundle metadata drops or misdeclares a module dependency.
@Suppress("unused")
fun smokeEntryPoint(): String = DiditSdk::class.java.name
