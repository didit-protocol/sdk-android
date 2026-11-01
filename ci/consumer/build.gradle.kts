// Versions mirror the producer (didit-protocol/mobile-sdks android/):
// AGP 8.6.0, Kotlin 2.1.10, Gradle 8.7 (the documented minimum toolchain).
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.10" apply false
}

// Always test the artifacts this commit actually ships: read the release
// version from the Maven metadata instead of hardcoding a version that would
// silently go stale after the next release. Shared by both consumer modules.
val diditSdkVersion by extra(
    rootProject.file("../../repository/me/didit/didit-sdk/maven-metadata.xml")
        .readText()
        .let { Regex("<release>([^<]+)</release>").find(it)?.groupValues?.get(1) }
        ?: error("could not read <release> from repository/me/didit/didit-sdk/maven-metadata.xml")
)
