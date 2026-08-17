plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Always test the artifacts this commit actually ships: read the release
// version from the Maven metadata instead of hardcoding a version that would
// silently go stale after the next release.
val diditSdkVersion: String = rootProject.file("../../repository/me/didit/didit-sdk/maven-metadata.xml")
    .readText()
    .let { Regex("<release>([^<]+)</release>").find(it)?.groupValues?.get(1) }
    ?: error("could not read <release> from repository/me/didit/didit-sdk/maven-metadata.xml")

android {
    namespace = "me.didit.ciconsumer"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.didit.ciconsumer"
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
    // Every published artifact must resolve and package into one app. The
    // didit-sdk bundle pulls core/autodetection/nfc/wallet through its POM;
    // didit-sdk-autodetection-play is opt-in and only covered by the explicit
    // line below.
    implementation("me.didit:didit-sdk:$diditSdkVersion")
    implementation("me.didit:didit-sdk-core:$diditSdkVersion")
    implementation("me.didit:didit-sdk-nfc:$diditSdkVersion")
    implementation("me.didit:didit-sdk-autodetection:$diditSdkVersion")
    implementation("me.didit:didit-sdk-autodetection-play:$diditSdkVersion")
    implementation("me.didit:didit-sdk-wallet:$diditSdkVersion")
}
