import dependencies.Library
import dependencies.TestLibrary
import dependencies.Version
import dependencies.App

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(Library.koin)
                implementation(Library.coroutines)
                implementation(Library.Ktor.ktor)

                with(Library.SqlDelight) {
                    implementation(sqldelight)
                    implementation(sqldelightExt)
                }
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
                implementation(Library.Ktor.ktorAndroid)
                implementation(Library.SqlDelight.sqldelightAndroid)
                implementation(Library.feedparser)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(TestLibrary.junit)
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
                implementation(Library.Ktor.ktorIOS)
                implementation(Library.SqlDelight.sqldelightIOS)
                implementation(Library.coroutines) {
                    version {
                        strictly(Version.coroutines)
                    }
                }
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
    namespace = "io.github.lazyengineer.castaway.data"
    compileSdk = App.compileSdk
    defaultConfig {
        minSdk = App.minSdk
        targetSdk = App.targetSdk
    }
}

sqldelight {
    database("CastawayDatabase") {
        packageName = "io.github.lazyengineer.castaway.db"
    }
}
