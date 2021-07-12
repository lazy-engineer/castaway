import dependencies.App
import dependencies.Library
import dependencies.TestLibrary

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("com.squareup.sqldelight")
  id("co.touchlab.native.cocoapods")
  id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
  android()
  val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
  if (onPhone) {
	iosArm64("ios")
  } else {
	iosX64("ios")
  }

  sourceSets {
	val commonMain by getting {
	  dependencies {
		implementation(Library.koin)
		implementation(Library.coroutines)
		implementation(Library.ktor)
		implementation(Library.sqldelight)
		implementation(Library.sqldelightExt)
		implementation(Library.stately)
		implementation(Library.isostate)
		implementation(Library.isostateCollection)
		api(Library.mokoResources)
	  }
	}
	val commonTest by getting {
	  dependencies {
		implementation(kotlin("test-common"))
		implementation(kotlin("test-annotations-common"))
		implementation(Library.koinTest)
	  }
	}
	val androidMain by getting {
	  dependencies {
		implementation(Library.material)
		implementation(Library.ktorAndroid)
		implementation(Library.sqldelightAndroid)
	  }
	}
	val androidTest by getting {
	  dependencies {
		implementation(kotlin("test-junit"))
		implementation(TestLibrary.junit)
	  }
	}
	val iosMain by getting {
	  dependencies {
		implementation(Library.ktorIOS)
		implementation(Library.sqldelightIOS)
		implementation(Library.mokoResources)
	  }
	}
	val iosTest by getting
  }

  cocoapodsext {
	summary = "Multiplatform shared library"
	homepage = "https://github.com/lazy-engineer/castaway"
	framework {
	  isStatic = false
	  transitiveExport = true
	}
  }
}

android {
  compileSdkVersion(App.compileSdk)
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
	minSdkVersion(App.minSdk)
	targetSdkVersion(App.targetSdk)
  }
}

sqldelight {
  database("CastawayDatabase") {
	packageName = "io.github.lazyengineer.castaway.db"
  }
}

multiplatformResources {
  multiplatformResourcesPackage = "io.github.lazyengineer.castaway.shared"
  disableStaticFrameworkWarning = true
}
