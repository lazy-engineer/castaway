import dependencies.App
import dependencies.Library

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("android.extensions")
  kotlin("kapt")
}

dependencies {
  implementation(project(":shared"))
  implementation(project(":castawayplayer"))

  add(org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME, Library.composeCompiler)
  implementation(Library.composeRuntime)
  implementation(Library.composeUi)
  implementation(Library.composeUiTooling)
  implementation(Library.composeFoundation)
  implementation(Library.composeMaterial)
  implementation(Library.composeMaterialIconsCore)
  implementation(Library.composeMaterialIconsExtended)
  implementation(Library.composeActivity)
  implementation(Library.composeViewModel)
  implementation(Library.composeNavigation)

  implementation(Library.viewmodelKtx)
  implementation(Library.activityKtx)
  implementation(Library.fragmentKtx)
  implementation(Library.material)
  implementation(Library.appcompat)
  implementation(Library.constraintlayout)
  implementation(Library.media)

  implementation(Library.koin)
  implementation(Library.koinAndroid)

  implementation(Library.coil)
  implementation(Library.coilCompose)
  implementation(Library.gson)
  implementation(Library.feedparser)
}

android {
  compileSdkVersion(App.compileSdk)
  defaultConfig {
	applicationId = "io.github.lazyengineer.castaway.androidApp"
	minSdkVersion(App.minSdk)
	targetSdkVersion(App.targetSdk)
	versionCode = App.versionCode
	versionName = App.versionName
  }

  buildTypes {
	getByName("release") {
	  isMinifyEnabled = false
	}
  }

  compileOptions {
	sourceCompatibility(JavaVersion.VERSION_1_8)
	targetCompatibility(JavaVersion.VERSION_1_8)
  }

  kotlinOptions {
	jvmTarget = "1.8"
	useIR = true
  }

  buildFeatures {
	dataBinding = true
  }
}