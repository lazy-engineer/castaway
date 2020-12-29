import dependencies.App
import dependencies.Library
import dependencies.TestLibrary
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    ios {
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
                implementation(Library.ktor)
                implementation(Library.sqldelight)
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
        sourceFolders = listOf("sqldelight")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)