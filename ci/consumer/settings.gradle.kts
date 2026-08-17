pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// The published artifacts carry BOTH Gradle module metadata (.module) and
// POMs, and Gradle prefers .module - so a malformed POM (what Maven and
// other non-Gradle consumers resolve) can hide behind a valid .module file.
// -PdiditPomOnlyMetadata makes this build resolve the didit repository from
// POMs alone, ignoring the published-with-gradle-metadata redirection marker,
// so CI validates both metadata formats.
val diditPomOnlyMetadata = providers.gradleProperty("diditPomOnlyMetadata").isPresent

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // The checked-out repository itself: CI must test the artifacts in
        // THIS commit, not whatever main currently serves over
        // raw.githubusercontent.com.
        maven {
            url = uri(rootDir.resolve("../../repository"))
            if (diditPomOnlyMetadata) {
                metadataSources {
                    mavenPom()
                    artifact()
                    ignoreGradleMetadataRedirection()
                }
            }
        }
        google()
        mavenCentral()
        // Transitive dependencies of the NFC artifact (see README installation).
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "didit-sdk-consumer"
include(":app-bundle")
include(":app-standalone")
