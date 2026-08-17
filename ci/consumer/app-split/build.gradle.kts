plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val diditSdkVersion: String by rootProject.extra

android {
    namespace = "me.didit.ciconsumer.split"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.didit.ciconsumer.split"
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
    // The five standalone artifacts WITHOUT the didit-sdk bundle: each must
    // resolve, compile, and package on its own POM/AAR, the way slim
    // integrations consume them.
    implementation("me.didit:didit-sdk-core:$diditSdkVersion")
    implementation("me.didit:didit-sdk-nfc:$diditSdkVersion")
    implementation("me.didit:didit-sdk-autodetection:$diditSdkVersion")
    implementation("me.didit:didit-sdk-autodetection-play:$diditSdkVersion")
    implementation("me.didit:didit-sdk-wallet:$diditSdkVersion")
}
