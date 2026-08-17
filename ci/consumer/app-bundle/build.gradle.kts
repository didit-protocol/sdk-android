plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val diditSdkVersion: String by rootProject.extra

android {
    namespace = "me.didit.ciconsumer.bundle"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.didit.ciconsumer.bundle"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // Documented integrator setup (README "Step 3: Add packaging exclusion"):
    // the Bouncy Castle jars pulled by the NFC artifact both ship this entry.
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

dependencies {
    // ONLY the bundle - nothing else may sit on this classpath. Any
    // additional dependency here (even the opt-in -play artifact, which
    // depends on core itself) would mask a broken bundle edge; -play is
    // covered by an app-standalone pass instead.
    implementation("me.didit:didit-sdk:$diditSdkVersion")
}

// The bundled modules other than core are internal-only (loaded reflectively
// by core), so a broken bundle edge cannot be caught by a compile-time
// reference. Assert at resolution level instead: every module the didit-sdk
// bundle promises must be a DIRECT dependency of the didit-sdk component
// itself - checking the whole resolved graph is not enough, because core
// also arrives transitively through the other modules' own edges and would
// mask a dropped direct edge.
val verifyBundleEdges by tasks.registering {
    val runtimeClasspath = configurations.named("debugRuntimeClasspath")
    inputs.files(runtimeClasspath)
    doLast {
        val bundle = runtimeClasspath.get()
            .incoming.resolutionResult.allComponents
            .find { it.moduleVersion?.group == "me.didit" && it.moduleVersion?.name == "didit-sdk" }
            ?: error("me.didit:didit-sdk did not resolve at all")
        val direct = bundle.dependencies
            .filterIsInstance<org.gradle.api.artifacts.result.ResolvedDependencyResult>()
            .mapNotNull { it.selected.moduleVersion }
            .map { "${it.group}:${it.name}" }
            .toSet()
        val missing = listOf("core", "autodetection", "nfc", "wallet")
            .map { "me.didit:didit-sdk-$it" }
            .filterNot { it in direct }
        require(missing.isEmpty()) {
            "didit-sdk bundle metadata is missing direct module edges: $missing (direct edges seen: $direct)"
        }
    }
}

tasks.matching { it.name == "assembleDebug" }.configureEach {
    dependsOn(verifyBundleEdges)
}
