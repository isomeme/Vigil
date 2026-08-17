plugins {
  id(libs.plugins.androidApplicationPlugin.get().pluginId)
  alias(libs.plugins.hiltAndroidPlugin)
  alias(libs.plugins.kotlinComposePlugin)
  alias(libs.plugins.kspPlugin)
}

android {
  namespace = "org.onereed.vigil"
  compileSdk { version = release(37) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "org.onereed.vigil"
    versionCode = 1
    versionName = "1.0"

    minSdk = 26
    targetSdk = 37

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

  // Compose BOM
  // See https://www.reddit.com/r/AndroidStudio/comments/1vnjxv4/comment/p3mmorh/
  @Suppress("AvoidDuplicateDependencies")
  implementation(platform(libs.composeBomLib))
  @Suppress("AvoidDuplicateDependencies")
  androidTestImplementation(platform(libs.composeBomLib))

  // org.onereed.shared library
  implementation(libs.onereedShared)

  // Hilt

  implementation(libs.bundles.hiltRuntime)
  ksp(libs.bundles.hiltKsp)

  // AndroidX and Compose

  implementation(libs.activityComposeLib)
  implementation(libs.collectionKtxLib)
  implementation(libs.coreKtxLib)
  implementation(libs.lifecycleLib)
  implementation(libs.workLib)
  implementation(libs.bundles.composeBomRuntime)

  // Guava

  implementation(libs.guavaLib)

  // Logging

  implementation(libs.timberLib)

  // Testing and debug

  testImplementation(libs.junitLib)

  androidTestImplementation(libs.espressoCoreLib)
  androidTestImplementation(libs.androidxJunitLib)
  androidTestImplementation(libs.uiTestJunit4Lib)

  debugImplementation(libs.uiTestManifestLib)
  debugImplementation(libs.uiToolingLib)
}
