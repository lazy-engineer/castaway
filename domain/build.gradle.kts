import dependencies.App
import dependencies.Library

plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

kotlin {
  androidTarget()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
	val commonMain by getting {
	  dependencies {
		implementation(Library.coroutines)
	  }
	}
	val commonTest by getting {
	  dependencies {
		implementation(kotlin("test"))
	  }
	}
	val androidMain by getting {
	  dependencies {
		implementation(Library.viewmodelKtx)
	  }
	}
	val androidUnitTest by getting

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
  compileSdk = App.compileSdk
  namespace = "io.github.lazyengineer.castaway.domain"
  defaultConfig {
	minSdk = App.minSdk
	targetSdk = App.targetSdk
  }

  compileOptions {
	sourceCompatibility(JavaVersion.VERSION_17)
	targetCompatibility(JavaVersion.VERSION_17)
  }
}
