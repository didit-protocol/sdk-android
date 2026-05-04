# DiditSDK for Android

A lightweight Android SDK for identity verification. Server-driven, minimal configuration required.

## Requirements

- Android API level 21+ (Android 5.0 Lollipop and above)
- Kotlin 1.9+
- Jetpack Compose (included as a transitive dependency)

### Feature Availability by API Level

| Feature | API 21-23 | API 24+ |
|---------|-----------|---------|
| Document auto-detection (ML) | No (falls back to manual capture) | Yes |
| Face auto-detection (ML) | No (falls back to manual capture) | Yes |
| Camera permissions | Install-time (API < 23) / Runtime (API 23+) | Runtime |

> **Note:** The SDK gracefully degrades on older devices. ML-based document and face auto-detection require API 24+ (MediaPipe) and fall back to manual capture with a timed button on older devices. All other features work across the full supported range.

## Permissions

The SDK requires the following permissions. These are declared in the SDK's `AndroidManifest.xml` and will be merged automatically into your app's manifest:

| Permission | Description |
|------------|-------------|
| `INTERNET` | Network access for API communication |
| `ACCESS_NETWORK_STATE` | Detect network availability |
| `CAMERA` | Document scanning and face verification |
| `NFC` | Read NFC chips in passports/ID cards |

### Camera and NFC Features

The SDK declares `android.hardware.camera` and `android.hardware.nfc` as optional features (`android:required="false"`). This ensures your app can be installed on devices without a camera or NFC hardware — the SDK will gracefully handle missing hardware at runtime.

## Installation

### Step 1: Add the repository

Add the Didit Maven repository to the `repositories` block in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://raw.githubusercontent.com/didit-protocol/sdk-android/main/repository") }
    }
}
```

Or if using `settings.gradle` (Groovy):

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url "https://raw.githubusercontent.com/didit-protocol/sdk-android/main/repository" }
    }
}
```

### Step 2: Add the dependency

Add the SDK dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("me.didit:didit-sdk:3.5.4")
}
```

Or if using `build.gradle` (Groovy):

```groovy
dependencies {
    implementation "me.didit:didit-sdk:3.5.4"
}
```

### Step 3: Add packaging exclusion

Add this to your app's `android` block to avoid build conflicts:

```kotlin
android {
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}
```

That's it! Gradle will automatically resolve all transitive dependencies.

## Quick Start

### Step 1: Initialize the SDK

Initialize the SDK in your `Application.onCreate()`:

```kotlin
import me.didit.sdk.DiditSdk

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DiditSdk.initialize(this)
    }
}
```

### Step 2: Observe SDK state and launch the UI

```kotlin
import me.didit.sdk.DiditSdk
import me.didit.sdk.DiditSdkState
import me.didit.sdk.VerificationResult

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observe SDK state to launch UI when ready
        lifecycleScope.launch {
            DiditSdk.state.collect { state ->
                when (state) {
                    is DiditSdkState.Ready -> DiditSdk.launchVerificationUI(this@MainActivity)
                    is DiditSdkState.Error -> Log.e("Didit", "Error: ${state.message}")
                    else -> { /* Loading, Idle, CreatingSession */ }
                }
            }
        }
    }

    private fun startVerification() {
        DiditSdk.startVerification(
            token = "your-session-token"
        ) { result ->
            when (result) {
                is VerificationResult.Completed -> {
                    Log.d("Didit", "Session: ${result.session.sessionId}")
                    Log.d("Didit", "Status: ${result.session.status.rawValue}")
                }
                is VerificationResult.Cancelled -> {
                    Log.d("Didit", "User cancelled")
                }
                is VerificationResult.Failed -> {
                    Log.e("Didit", "Failed: ${result.error.message}")
                }
            }
        }
    }
}
```

### Starting verification

There are two ways to start the verification flow:

#### Option A: With existing session token (from your backend)

```kotlin
// Your backend creates the session and returns a token
DiditSdk.startVerification(
    token = "your-session-token"
) { result ->
    handleResult(result)
}
```

#### Option B: With workflow ID (SDK creates session)

```kotlin
// SDK creates the session using your workflow ID
DiditSdk.startVerification(
    workflowId = "your-workflow-id",
    vendorData = "user-123"  // Optional: your user identifier
) { result ->
    handleResult(result)
}
```

## Configuration

You can customize the SDK behavior using `Configuration`:

```kotlin
import me.didit.sdk.Configuration
import me.didit.sdk.core.localization.SupportedLanguage

val configuration = Configuration(
    languageLocale = SupportedLanguage.SPANISH,  // Force Spanish language
    fontFamily = "my_custom_font",                // Custom font (must be in res/font/)
    loggingEnabled = true                         // Enable debug logging
)

DiditSdk.startVerification(
    token = "your-session-token",
    configuration = configuration
) { result ->
    handleResult(result)
}
```

### Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `languageLocale` | `SupportedLanguage?` | Device locale | Force a specific language |
| `fontFamily` | `String?` | System font | Custom font resource name (from `res/font/`) |
| `loggingEnabled` | `Boolean` | `false` | Enable SDK debug logging |

### Language Settings

The SDK supports 53 languages. If no language is specified, the SDK uses the device locale with English fallback.

```kotlin
// Use device locale (default)
val config = Configuration()

// Force specific language
val config = Configuration(languageLocale = SupportedLanguage.FRENCH)
```

#### Supported Languages

| Language | Value | Language | Value |
|----------|------|----------|------|
| Albanian | `ALBANIAN` | Kazakh | `KAZAKH` |
| Arabic | `ARABIC` | Korean | `KOREAN` |
| Armenian | `ARMENIAN` | Kyrgyz | `KYRGYZ` |
| Bengali | `BENGALI` | Latvian | `LATVIAN` |
| Bosnian | `BOSNIAN` | Lithuanian | `LITHUANIAN` |
| Bulgarian | `BULGARIAN` | Macedonian | `MACEDONIAN` |
| Catalan | `CATALAN` | Malay | `MALAY` |
| Chinese | `CHINESE` | Montenegrin | `MONTENEGRIN` |
| Chinese (Simplified) | `CHINESE_SIMPLIFIED` | Norwegian | `NORWEGIAN` |
| Chinese (Traditional) | `CHINESE_TRADITIONAL` | Persian | `PERSIAN` |
| Croatian | `CROATIAN` | Polish | `POLISH` |
| Czech | `CZECH` | Portuguese | `PORTUGUESE` |
| Danish | `DANISH` | Portuguese (Brazil) | `PORTUGUESE_BRAZIL` |
| Dutch | `DUTCH` | Romanian | `ROMANIAN` |
| English | `ENGLISH` | Russian | `RUSSIAN` |
| Estonian | `ESTONIAN` | Serbian | `SERBIAN` |
| Finnish | `FINNISH` | Slovak | `SLOVAK` |
| French | `FRENCH` | Slovenian | `SLOVENIAN` |
| Georgian | `GEORGIAN` | Somali | `SOMALI` |
| German | `GERMAN` | Spanish | `SPANISH` |
| Greek | `GREEK` | Swedish | `SWEDISH` |
| Hebrew | `HEBREW` | Thai | `THAI` |
| Hindi | `HINDI` | Turkish | `TURKISH` |
| Hungarian | `HUNGARIAN` | Ukrainian | `UKRAINIAN` |
| Indonesian | `INDONESIAN` | Uzbek | `UZBEK` |
| Italian | `ITALIAN` | Vietnamese | `VIETNAMESE` |
| Japanese | `JAPANESE` |  |  |

## Advanced Options

### Contact Details (Prefill)

Provide contact details to prefill verification forms and enable notifications:

```kotlin
import me.didit.sdk.models.ContactDetails

val contactDetails = ContactDetails(
    email = "user@example.com",
    sendNotificationEmails = true,  // Send status update emails
    emailLang = "en",               // Email language
    phone = "+14155552671"          // E.164 format
)

DiditSdk.startVerification(
    workflowId = "your-workflow-id",
    contactDetails = contactDetails
) { result ->
    handleResult(result)
}
```

### Expected Details (Cross-Validation)

Provide expected user details for cross-validation with extracted data:

```kotlin
import me.didit.sdk.models.ExpectedDetails

val expectedDetails = ExpectedDetails(
    firstName = "John",
    lastName = "Doe",
    dateOfBirth = "1990-05-15",   // YYYY-MM-DD format
    nationality = "USA",          // ISO 3166-1 alpha-3
    country = "USA"
)

DiditSdk.startVerification(
    workflowId = "your-workflow-id",
    expectedDetails = expectedDetails
) { result ->
    handleResult(result)
}
```

### Metadata

Store custom JSON metadata with the session (not displayed to user):

```kotlin
DiditSdk.startVerification(
    workflowId = "your-workflow-id",
    vendorData = "user-123",
    metadata = "{\"internalId\": \"abc123\", \"source\": \"mobile\"}"
) { result ->
    handleResult(result)
}
```

## Verification Results

The `VerificationResult` sealed class provides the outcome of the verification:

### Result Cases

| Case | Description |
|------|-------------|
| `Completed(session)` | Verification flow completed (approved, pending, or declined) |
| `Cancelled(session)` | User cancelled the verification flow |
| `Failed(error, session)` | An error occurred during verification |

### SessionData

Each result includes a `SessionData` object:

| Property | Type | Description |
|----------|------|-------------|
| `sessionId` | `String` | The unique session identifier |
| `status` | `VerificationStatus` | Status: `APPROVED`, `PENDING`, `DECLINED` |

### VerificationError

| Error | Description |
|-------|-------------|
| `SessionExpired` | The session has expired |
| `NetworkError` | Network connectivity issue |
| `CameraAccessDenied` | Camera permission not granted |
| `NotInitialized` | SDK not initialized |
| `Unknown(message)` | Other error with message |

### Example Usage

```kotlin
DiditSdk.startVerification(token = "your-token") { result ->
    when (result) {
        is VerificationResult.Completed -> {
            when (result.session.status) {
                VerificationStatus.APPROVED -> println("Approved!")
                VerificationStatus.PENDING -> println("Under review")
                VerificationStatus.DECLINED -> println("Declined")
            }
        }
        is VerificationResult.Cancelled -> {
            println("Cancelled: ${result.session?.sessionId ?: "unknown"}")
        }
        is VerificationResult.Failed -> {
            println("Error: ${result.error.message}")
        }
    }
}
```

## ProGuard / R8

The SDK includes its own consumer ProGuard rules. No additional configuration is needed.

If you use R8 full mode, you may need to add this to your `gradle.properties`:

```properties
android.enableR8.fullMode=false
```

## License

Copyright (c) 2026 Didit. All rights reserved.
