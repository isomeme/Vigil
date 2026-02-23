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

      ndk { debugSymbolLevel = "FULL" }
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

  buildFeatures {
    buildConfig = true
    compose = true
  }
}

dependencies {

  // Required for Java 8+ APIs on API levels < 33

  coreLibraryDesugaring(libs.desugarLib)

  // Hilt

  implementation(libs.bundles.hiltRuntime)
  ksp(libs.bundles.hiltKsp)

  // AndroidX and Compose

  implementation(libs.activityComposeLib)
  implementation(libs.coreKtxLib)
  implementation(libs.lifecycleLib)
  implementation(libs.workLib)

  implementation(platform(libs.composeBomLib))
  implementation(libs.bundles.composeBomRuntime)

  // Logging

  implementation(libs.timberLib)

  // Testing and debug

  testImplementation(libs.junitLib)

  androidTestImplementation(platform(libs.composeBomLib))
  androidTestImplementation(libs.espressoCoreLib)
  androidTestImplementation(libs.androidxJunitLib)
  androidTestImplementation(libs.uiTestJunit4Lib)

  debugImplementation(libs.uiTestManifestLib)
  debugImplementation(libs.uiToolingLib)
}
