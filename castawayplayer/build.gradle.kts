import dependencies.AndroidTestLibrary
import dependencies.App
import dependencies.Library
import dependencies.TestLibrary

plugins {
  id("com.android.library")
  id("kotlin-android")
}

android {
  compileSdk = App.compileSdk
  buildToolsVersion = App.buildTools

  defaultConfig {
	minSdk = App.minSdk
	targetSdk = App.targetSdk

	testInstrumentationRunner = App.testRunner
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
	sourceCompatibility(JavaVersion.VERSION_11)
	targetCompatibility(JavaVersion.VERSION_11)
  }

  kotlinOptions {
	jvmTarget = "11"
  }
  namespace = "io.github.lazyengineer.castawayplayer"
}

dependencies {
  implementation(Library.kotlin)
  implementation(Library.ktxCore)

  implementation(Library.exoplayer)
  implementation(Library.exoplayerCast)
  implementation(Library.exoplayerMediaSession)
  implementation(Library.exoplayerUI)
  implementation(Library.coil)
  implementation(Library.gson)

  implementation(Library.coroutines)
  implementation(Library.coroutinesAndroid)

  testImplementation(TestLibrary.junit)
  androidTestImplementation(AndroidTestLibrary.androidJunit)
}