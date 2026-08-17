plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val diditSdkVersion: String by rootProject.extra

// Which single standalone artifact to consume, e.g.
//   gradle :app-standalone:assembleDebug -PdiditStandaloneArtifact=nfc
// One artifact per build so a missing dependency edge in one artifact can
// never be masked by another artifact on the same classpath.
val diditStandaloneArtifact: String =
    providers.gradleProperty("diditStandaloneArtifact").getOrElse("core")

android {
    namespace = "me.didit.ciconsumer.standalone"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.didit.ciconsumer.standalone"
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
    implementation("me.didit:didit-sdk-$diditStandaloneArtifact:$diditSdkVersion")
}
