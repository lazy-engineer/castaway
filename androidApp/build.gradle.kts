import config.App

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

dependencies {
  implementation(project(":shared"))
  implementation(project(":castawayplayer"))

  with(libs) {
    implementation(compose.ui)
    implementation(compose.ui.tooling)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material.icons.core)
    implementation(compose.material.icons.extended)
    implementation(constraintlayout.compose)
    implementation(activity.compose)
    implementation(lifecycle.runtime.ktx)
    implementation(lifecycle.viewmodel.compose)
    implementation(navigation.compose)
    implementation(lifecycle.runtime.compose)
    implementation(work.runtime.ktx)
    implementation(lifecycle.viewmodel.ktx)
    implementation(activity.ktx)
    implementation(fragment.ktx)
    implementation(material)
    implementation(appcompat)
    implementation(media)
    implementation(koin.core)
    implementation(koin.android)
    implementation(koin.androidx.compose)
    implementation(coil)
    implementation(coil.compose)
    implementation(gson)
    implementation(kotlinx.collections.immutable)

    androidTestImplementation(compose.ui.test)
    androidTestImplementation(compose.ui.test.junit)
    debugImplementation(compose.ui.test.manifest)

    testImplementation(junit)
    testImplementation(kotlinx.coroutines.test)
    testImplementation(turbine)
    testImplementation(mockito.kotlin)
    testImplementation(mockk)
    testImplementation(kluent)
    testImplementation(kotest.runner.junit5)
  }

  testImplementation(project(mapOf("path" to ":data")))
}

android {
  compileSdk = App.compileSdk
  defaultConfig {
    applicationId = App.applicationId
    minSdk = App.minSdk
    targetSdk = App.targetSdk
    versionCode = App.versionCode
    versionName = App.versionName
    testInstrumentationRunner = App.testRunner
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility(App.javaVersion)
    targetCompatibility(App.javaVersion)
  }

  kotlinOptions {
    jvmTarget = App.jvmTarget
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = App.kotlinCompilerVersion
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

  namespace = App.namespace
}
