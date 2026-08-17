pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // The checked-out repository itself: CI must test the artifacts in
        // THIS commit, not whatever main currently serves over
        // raw.githubusercontent.com.
        maven { url = uri(rootDir.resolve("../../repository")) }
        google()
        mavenCentral()
        // Transitive dependencies of the NFC artifact (see README installation).
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "didit-sdk-consumer"
include(":app")
