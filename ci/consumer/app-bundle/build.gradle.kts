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
    // ONLY the bundle (plus the opt-in -play artifact the bundle deliberately
    // does not pull). The didit-sdk AAR is a pure aggregator with an empty
    // classes.jar, so compiling Smoke.kt's DiditSdk reference proves the
    // bundle POM's transitive edges (core/nfc/autodetection/wallet) are
    // intact. Do NOT add the standalone artifacts here - they would mask a
    // broken bundle edge (that is what app-split covers separately).
    implementation("me.didit:didit-sdk:$diditSdkVersion")
    implementation("me.didit:didit-sdk-autodetection-play:$diditSdkVersion")
}
