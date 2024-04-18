import config.Shared
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform")
  kotlin("native.cocoapods")
  id("com.android.library")
}

kotlin {
  androidTarget()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  cocoapods {
    summary = "Multiplatform shared library"
    homepage = "https://github.com/lazy-engineer/castaway"
    version = "1.0"
    ios.deploymentTarget = "14.1"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "shared"
      export(project(":domain"))
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(project(":domain"))
        implementation(project(":data"))
        implementation(libs.koin.core)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(libs.koin.test)
      }
    }
    val androidMain by getting
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
  compileSdk = Shared.compileSdk
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = Shared.minSdk
    targetSdk = Shared.targetSdk
  }
  namespace = Shared.namespace

  compileOptions {
    sourceCompatibility(Shared.javaVersion)
    targetCompatibility(Shared.javaVersion)
  }
}

project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
  targets
    .filterIsInstance<KotlinNativeTarget>()
    .flatMap { it.binaries }
    .forEach { compilationUnit -> compilationUnit.linkerOpts("-lsqlite3") }
}
