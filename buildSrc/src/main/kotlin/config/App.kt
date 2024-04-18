package config

import org.gradle.api.JavaVersion

object App {

  const val applicationId = "io.github.lazyengineer.castaway"
  const val namespace = "io.github.lazyengineer.castaway.androidApp"

  const val compileSdk = 34
  const val minSdk = 24
  const val targetSdk = 34
  const val versionCode = 1
  const val versionName = "1.0.0"

  const val buildTools = "33.0.1"
  const val testRunner = "androidx.test.runner.AndroidJUnitRunner"

  const val kotlinCompilerVersion = "1.5.3"

  val javaVersion = JavaVersion.VERSION_17
  val jvmTarget = javaVersion.majorVersion
}
