plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "org.onereed.vigil"
  compileSdk { version = release(36) }

  defaultConfig {
    applicationId = "org.onereed.vigil"
    minSdk = 26
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    val commonProguardFiles =
      listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

    getByName("release") {
      versionNameSuffix = ""

      isMinifyEnabled = true
      isShrinkResources = true
      setProguardFiles(commonProguardFiles)

      ndk {
        debugSymbolLevel = "FULL"
      }
    }

    getByName("debug") { versionNameSuffix = " (debug)" }

    create("staging") {
      versionNameSuffix = " (staging)"

      initWith(getByName("debug"))
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      setProguardFiles(commonProguardFiles)
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    isCoreLibraryDesugaringEnabled = true
  }

  buildFeatures { compose = true }
}

dependencies {

  // Required for Java 8+ APIs on API levels < 33
  coreLibraryDesugaring(libs.desugarJdkLibsNio)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
}
