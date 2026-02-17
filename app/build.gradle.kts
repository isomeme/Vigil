plugins {
  alias(libs.plugins.androidApplicationPlugin)
  alias(libs.plugins.hiltAndroidPlugin)
  alias(libs.plugins.kotlinComposePlugin)
  alias(libs.plugins.kspPlugin)
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

  // Hilt

  implementation(libs.bundles.hiltRuntime)
  ksp(libs.hiltAndroidCompilerKsp)

  // AndroidX and Compose

  implementation(libs.androidxCoreKtx)
  implementation(libs.androidxLifecycleRuntimeKtx)
  implementation(libs.androidxActivityCompose)

  implementation(platform(libs.composeBom))
  implementation(libs.bundles.composeBomRuntime)

  // Testing and debug

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidxJunit)
  androidTestImplementation(libs.androidxEspressoCore)
  androidTestImplementation(platform(libs.composeBom))
  androidTestImplementation(libs.composeUiTestJunit4)
  debugImplementation(libs.composeUiTooling)
  debugImplementation(libs.androidxComposeUiTestManifest)
}
