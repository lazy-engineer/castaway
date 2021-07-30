package dependencies

object App {

  const val compileSdk = 30
  const val minSdk = 21
  const val targetSdk = 30
  const val versionCode = 1
  const val versionName = "1.0.0"

  const val buildTools = "30.0.2"
  const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Version {

  const val kotlin = "1.5.10"
  const val gradle = "7.0.0"

  const val appcompat = "1.3.0"
  const val constraintlayout = "2.0.4"
  const val material = "1.2.1"
  const val media = "1.2.1"

  const val lifecycleExtensions = "2.2.0"
  const val ktxActivity = "1.1.0"
  const val ktxFragment = "1.2.5"
  const val compose = "1.0.0"
  const val composeNavigation = "2.4.0-alpha01"

  const val ktxCore = "1.3.2"
  const val coroutines = "1.5.0-native-mt"
  const val exoplayer = "2.14.0"
  const val coil = "1.1.0"
  const val coilCompose = "0.10.0"
  const val gson = "2.8.6"
  const val koin = "3.1.2"
  const val ktor = "1.6.0"
  const val sqldelight = "1.5.0"
  const val feedparser = "0.1.0"
  const val stately = "1.1.4"
  const val isostate = "1.1.4-a1"

  const val junit = "4.13.1"
  const val androidJunit = "1.1.2"
}

object Plugin {

  const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
  const val gradle = "com.android.tools.build:gradle:${Version.gradle}"
  const val koin = "org.koin:koin-gradle-plugin:${Version.koin}"
}

object Library {

  const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"
  const val ktxCore = "androidx.core:core-ktx:${Version.ktxCore}"

  const val appcompat = "androidx.appcompat:appcompat:${Version.appcompat}"
  const val material = "com.google.android.material:material:${Version.material}"
  const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Version.constraintlayout}"

  const val koin = "io.insert-koin:koin-core:${Version.koin}"
  const val koinTest = "io.insert-koin:koin-test:${Version.koin}"
  const val koinAndroid = "io.insert-koin:koin-android:${Version.koin}"

  const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycleExtensions}"
  const val activityKtx = "androidx.activity:activity-ktx:${Version.ktxActivity}"
  const val fragmentKtx = "androidx.fragment:fragment-ktx:${Version.ktxFragment}"

  const val media = "androidx.media:media:${Version.media}"
  const val exoplayer = "com.google.android.exoplayer:exoplayer:${Version.exoplayer}"
  const val exoplayerMediaSession = "com.google.android.exoplayer:extension-mediasession:${Version.exoplayer}"
  const val exoplayerCast = "com.google.android.exoplayer:extension-cast:${Version.exoplayer}"
  const val exoplayerUI = "com.google.android.exoplayer:exoplayer-ui:${Version.exoplayer}"

  const val coil = "io.coil-kt:coil:${Version.coil}"
  const val coilCompose = "com.google.accompanist:accompanist-coil:${Version.coilCompose}"
  const val gson = "com.google.code.gson:gson:${Version.gson}"
  const val ktor = "io.ktor:ktor-client-core:${Version.ktor}"
  const val ktorAndroid = "io.ktor:ktor-client-android:${Version.ktor}"
  const val ktorIOS = "io.ktor:ktor-client-ios:${Version.ktor}"
  const val sqldelight = "com.squareup.sqldelight:runtime:${Version.sqldelight}"
  const val sqldelightExt = "com.squareup.sqldelight:coroutines-extensions:${Version.sqldelight}"
  const val sqldelightAndroid = "com.squareup.sqldelight:android-driver:${Version.sqldelight}"
  const val sqldelightIOS = "com.squareup.sqldelight:native-driver:${Version.sqldelight}"
  const val feedparser = "io.github.lazy-engineer:feedparser:${Version.feedparser}"
  const val stately = "co.touchlab:stately-common:${Version.stately}"
  const val isostate = "co.touchlab:stately-isolate:${Version.isostate}"
  const val isostateCollection = "co.touchlab:stately-iso-collections:${Version.isostate}"

  const val composeCompiler = "androidx.compose.compiler:compiler:${Version.compose}"
  const val composeRuntime = "androidx.compose.runtime:runtime:${Version.compose}"
  const val composeUi = "androidx.compose.ui:ui:${Version.compose}"
  const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Version.compose}"
  const val composeFoundation = "androidx.compose.foundation:foundation:${Version.compose}"
  const val composeMaterial = "androidx.compose.material:material:${Version.compose}"
  const val composeMaterialIconsCore = "androidx.compose.material:material-icons-core:${Version.compose}"
  const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended:${Version.compose}"
  const val composeActivity = "androidx.activity:activity-compose:1.3.0-alpha03"
  const val composeLifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01"
  const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha02"
  const val composeNavigation = "androidx.navigation:navigation-compose:${Version.composeNavigation}"
  const val composeUiTests = "androidx.compose.ui:ui-test-junit4:${Version.compose}"

  const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
  const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
}

object TestLibrary {

  const val junit = "junit:junit:${Version.junit}"
}

object AndroidTestLibrary {

  const val androidJunit = "androidx.test.ext:junit:${Version.androidJunit}"
}
