import config.CastawayPlayer

plugins {
  id("com.android.library")
  id("kotlin-android")
}

android {
  compileSdk = CastawayPlayer.compileSdk
  buildToolsVersion = CastawayPlayer.buildTools

  defaultConfig {
    minSdk = CastawayPlayer.minSdk
    targetSdk = CastawayPlayer.targetSdk

    testInstrumentationRunner = CastawayPlayer.testRunner
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    buildTypes {
      getByName("release") {
        isMinifyEnabled = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      }
    }
  }

  compileOptions {
    sourceCompatibility(CastawayPlayer.javaVersion)
    targetCompatibility(CastawayPlayer.javaVersion)
  }

  kotlinOptions {
    jvmTarget = CastawayPlayer.jvmTarget
  }
  namespace = CastawayPlayer.namespace
}

dependencies {
  with(libs) {
    implementation(kotlin.stdlib)
    implementation(core.ktx)
    implementation(exoplayer)
    implementation(exoplayer.extension.cast)
    implementation(exoplayer.extension.mediasession)
    implementation(exoplayer.ui)
    implementation(coil)
    implementation(gson)
    implementation(kotlinx.coroutines.core)
    implementation(kotlinx.coroutines.android)

    testImplementation(junit)
    androidTestImplementation(android.junit)
  }
}