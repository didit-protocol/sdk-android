package me.didit.ciconsumer.standalone

import me.didit.sdk.DiditSdk

// DiditSdk lives in didit-sdk-core; every standalone artifact must provide it
// through its own POM/module edge to core (or directly, for core itself), so
// this compiles only when the single artifact under test resolves on its own.
@Suppress("unused")
fun smokeEntryPoint(): String = DiditSdk::class.java.name
