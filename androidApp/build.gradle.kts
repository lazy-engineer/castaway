import dependencies.AndroidTestLibrary
import dependencies.App
import dependencies.Library
import dependencies.TestLibrary

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

dependencies {
  implementation(project(":shared"))
  implementation(project(":castawayplayer"))

  with(Library.Compose) {
    implementation(composeUi)
    implementation(composeUiTooling)
    implementation(composeFoundation)
    implementation(composeMaterial)
    implementation(composeMaterialIconsCore)
    implementation(composeMaterialIconsExtended)
    implementation(composeConstraintlayout)
    implementation(composeActivity)
    implementation(composeLifecycle)
    implementation(composeViewModel)
    implementation(composeNavigation)
    implementation(composeLifecycleRuntime)
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
    implementation(koinCompose)

    implementation(coil)
    implementation(coilCompose)
    implementation(gson)

    implementation(immutableCollections)
  }

  with(AndroidTestLibrary) {
    androidTestImplementation(composeUiTest)
    androidTestImplementation(composeUiTestJunit)
    debugImplementation(composeDebugTestManifest)
  }

  with(TestLibrary) {
    testImplementation(junit)
    testImplementation(coroutines)
    testImplementation(turbine)
    testImplementation(mockito)
    testImplementation(mockk)
    testImplementation(kluent)
    testImplementation(kotest)
  }

  testImplementation(project(mapOf("path" to ":data")))
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
    sourceCompatibility(JavaVersion.VERSION_17)
    targetCompatibility(JavaVersion.VERSION_17)
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
  }

  packaging {
    resources.excludes.add("META-INF/licenses/**")
    resources.excludes.add("META-INF/AL2.0")
    resources.excludes.add("META-INF/LGPL2.1")
  }

  testOptions {
    unitTests.all {
      it.useJUnitPlatform()
    }
  }

  namespace = "io.github.lazyengineer.castaway.androidApp"
}
