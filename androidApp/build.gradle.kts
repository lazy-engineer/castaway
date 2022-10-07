import dependencies.AndroidTestLibrary
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

  with(Library.Compose) {
	implementation(composeRuntime)
	implementation(composeUi)
	implementation(composeUiTooling)
	implementation(composeFoundation)
	implementation(composeMaterial)
	implementation(composeMaterialIconsCore)
	implementation(composeMaterialIconsExtended)
	implementation(composeActivity)
	implementation(composeLifecycle)
	implementation(composeViewModel)
	implementation(composeNavigation)
  }

  with(Library) {
	implementation(ktxWorkRuntime)
	implementation(viewmodelKtx)
	implementation(activityKtx)
	implementation(fragmentKtx)
	implementation(material)
	implementation(appcompat)
	implementation(media)

	implementation(koin)
	implementation(koinAndroid)

	implementation(coil)
	implementation(coilCompose)
	implementation(gson)
  }

  with(AndroidTestLibrary) {
	androidTestImplementation(composeUiTest)
	androidTestImplementation(composeUiTestJunit)
	debugImplementation(composeDebugTestManifest)
  }
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
	kotlinCompilerExtensionVersion = "1.3.2"
  }

  packagingOptions {
	resources.excludes.add("META-INF/licenses/**")
	resources.excludes.add("META-INF/AL2.0")
	resources.excludes.add("META-INF/LGPL2.1")
  }
  namespace = "io.github.lazyengineer.castaway.androidApp"
}
