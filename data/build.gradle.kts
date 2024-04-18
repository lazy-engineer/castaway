import config.Shared

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("com.squareup.sqldelight")
}

kotlin {
  androidTarget()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":domain"))
        implementation(libs.koin.core)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.ktor.client.core)
        implementation(libs.sqldelight.runtime)
        implementation(libs.sqldelight.coroutines.extensions)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(libs.koin.test)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.ktor.client.android)
        implementation(libs.sqldelight.android.driver)
        implementation(libs.feedparser)
      }
    }
    val androidUnitTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation(libs.junit)
      }
    }

    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)
      dependencies {
        implementation(libs.ktor.client.darwin)
        implementation(libs.sqldelight.native.driver)
        implementation(libs.kotlinx.coroutines.core)
      }
    }

    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosTest by creating {
      dependsOn(commonTest)
      iosX64Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
    }
  }
}

android {
  namespace = Shared.Data.namespace
  compileSdk = Shared.compileSdk
  defaultConfig {
    minSdk = Shared.minSdk
    targetSdk = Shared.targetSdk
  }

  compileOptions {
    sourceCompatibility(Shared.javaVersion)
    targetCompatibility(Shared.javaVersion)
  }
}

sqldelight {
  database(Shared.Data.database) {
    packageName = Shared.Data.databasePackageName
  }
}
