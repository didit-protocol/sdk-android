package me.didit.ciconsumer

import me.didit.sdk.DiditSdk

// Compile-time reference to the SDK entry point: catches a published AAR whose
// classes.jar is missing or whose public API surface broke, not just POM
// resolution.
@Suppress("unused")
fun smokeEntryPoint(): String = DiditSdk::class.java.name
