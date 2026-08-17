package me.didit.ciconsumer.split

import me.didit.sdk.DiditSdk

// Compile-time reference to the SDK entry point (didit-sdk-core): catches a
// published AAR whose classes.jar is missing or whose public API broke, not
// just POM resolution.
@Suppress("unused")
fun smokeEntryPoint(): String = DiditSdk::class.java.name
