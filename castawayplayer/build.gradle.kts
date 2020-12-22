import dependencies.AndroidTestLibrary
import dependencies.App
import dependencies.Library
import dependencies.TestLibrary

plugins {
	id("com.android.library")
	id("kotlin-android")
}

android {
	compileSdkVersion(App.compileSdk)
	buildToolsVersion(App.buildTools)

	defaultConfig {
		minSdkVersion(App.minSdk)
		targetSdkVersion(App.targetSdk)
		versionCode = App.versionCode
		versionName = App.versionName

		testInstrumentationRunner(App.testRunner)
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		buildTypes {
			getByName("release") {
				isMinifyEnabled = false
				proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
			}
		}
	}

	compileOptions {
		sourceCompatibility(JavaVersion.VERSION_1_8)
		targetCompatibility(JavaVersion.VERSION_1_8)
	}

	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
	implementation(Library.kotlin)
	implementation(Library.ktxCore)

	implementation(Library.exoplayer)
	implementation(Library.exoplayerCast)
	implementation(Library.exoplayerMediaSession)
	implementation(Library.exoplayerUI)
	implementation(Library.coil)
	implementation(Library.gson)

	implementation(Library.coroutines)
	implementation(Library.coroutinesAndroid)

	testImplementation(TestLibrary.junit)
	androidTestImplementation(AndroidTestLibrary.androidJunit)
}