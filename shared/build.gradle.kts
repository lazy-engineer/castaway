import dependencies.App
import dependencies.Library
import dependencies.TestLibrary
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("com.squareup.sqldelight")
  id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
  android()
  val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
	if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
	  ::iosArm64
	else
	  ::iosX64

  iOSTarget("ios") {
	binaries {
	  framework {
		baseName = "shared"
	  }
	}
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
	  }
	}
	val iosTest by getting
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
}

val packForXcode by tasks.creating(Sync::class) {
  group = "build"
  val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
  val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
  inputs.property("mode", mode)
  dependsOn(framework.linkTask)
  val targetDir = File(buildDir, "xcode-frameworks")
  from({ framework.outputDirectory })
  into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)