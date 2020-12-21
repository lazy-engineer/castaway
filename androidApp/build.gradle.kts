import dependencies.App
import dependencies.Library

plugins {
	id("com.android.application")
	kotlin("android")
	id("koin")
}

dependencies {
	implementation(project(":shared"))

	implementation(Library.material)
	implementation(Library.appcompat)
	implementation(Library.constraintlayout)
	implementation(Library.koin)
	implementation(Library.koinExt)
}

android {
	compileSdkVersion(App.compileSdk)
	defaultConfig {
		applicationId = "io.github.lazyengineer.castaway.androidApp"
		minSdkVersion(App.minSdk)
		targetSdkVersion(App.targetSdk)
		versionCode = App.versionCode
		versionName = App.versionName
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
}