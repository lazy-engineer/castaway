import dependencies.App
import dependencies.Library
import dependencies.TestLibrary
import dependencies.Version

plugins {
  kotlin("multiplatform")
  kotlin("native.cocoapods")
  id("com.android.library")
  id("com.squareup.sqldelight")
}

// CocoaPods requires the podspec to have a version.
version = "1.0"

kotlin {
  android()
  ios()

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
		implementation(Library.coroutines) {
		  version {
			strictly(Version.coroutines)
		  }
		}
	  }
	}
	val iosTest by getting
  }

  cocoapods {
	summary = "Multiplatform shared library"
	homepage = "https://github.com/lazy-engineer/castaway"
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
