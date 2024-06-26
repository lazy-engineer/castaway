
import dependencies.AndroidTestLibrary
import dependencies.App
import dependencies.Library
import dependencies.Version

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
  implementation(Library.composeLifecycle)
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

  androidTestImplementation(AndroidTestLibrary.composeUiTest)
  androidTestImplementation(AndroidTestLibrary.composeUiTestJunit)
  debugImplementation(AndroidTestLibrary.composeDebugTestManifest)
}

android {
  compileSdk = App.compileSdk
  defaultConfig {
	applicationId = "io.github.lazyengineer.castaway.androidApp"
	minSdk = App.minSdk
	targetSdk = App.targetSdk
	versionCode = App.versionCode
	versionName = App.versionName
	testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
	getByName("release") {
	  isMinifyEnabled = false
	}
  }

  compileOptions {
	sourceCompatibility(JavaVersion.VERSION_11)
	targetCompatibility(JavaVersion.VERSION_11)
  }

  kotlinOptions {
	jvmTarget = "11"
  }

  buildFeatures {
	compose = true
  }

  composeOptions {
	kotlinCompilerExtensionVersion = Version.compose
  }

  packagingOptions {
	resources.excludes.add("META-INF/licenses/**")
	resources.excludes.add("META-INF/AL2.0")
	resources.excludes.add("META-INF/LGPL2.1")
  }
}
