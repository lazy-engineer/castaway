package dependencies

object App {

  const val compileSdk = 33
  const val minSdk = 21
  const val targetSdk = 33
  const val versionCode = 1
  const val versionName = "1.0.0"

  const val buildTools = "30.0.3"
  const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Version {

  const val kotlin = "1.7.20"
  const val gradle = "7.2.2"

  const val appcompat = "1.5.1"
  const val material = "1.2.1"
  const val media = "1.6.0"

  const val lifecycleExtensions = "2.5.1"
  const val ktxActivity = "1.5.1"

  const val ktxFragment = "1.5.2"
  const val ktxLifecycleRuntime = "2.5.1"
  const val compose = "1.2.1"
  const val composeActivity = "1.5.1"
  const val composeViewModel = "2.5.1"
  const val composeNavigation = "2.5.2"

  const val ktxCore = "1.9.0"
  const val ktxWorkRuntime = "2.7.1"
  const val coroutines = "1.6.4"
  const val exoplayer = "2.14.0"
  const val coil = "1.1.0"
  const val coilCompose = "0.10.0"
  const val gson = "2.9.1"
  const val koin = "3.2.0"
  const val ktor = "2.1.2"
  const val sqldelight = "1.5.3"
  const val feedparser = "0.1.0"

  const val junit = "4.13.1"
  const val androidJunit = "1.1.2"
}

object Plugin {

  const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
  const val gradle = "com.android.tools.build:gradle:${Version.gradle}"
  const val koin = "org.koin:koin-gradle-plugin:${Version.koin}"
  const val sqldelight = "com.squareup.sqldelight:gradle-plugin:${Version.sqldelight}"
}

object Library {

  const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"
  const val ktxCore = "androidx.core:core-ktx:${Version.ktxCore}"
  const val ktxWorkRuntime = "androidx.work:work-runtime-ktx:${Version.ktxWorkRuntime}"

  const val appcompat = "androidx.appcompat:appcompat:${Version.appcompat}"
  const val material = "com.google.android.material:material:${Version.material}"

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

  object Ktor {
    const val ktor = "io.ktor:ktor-client-core:${Version.ktor}"
    const val ktorAndroid = "io.ktor:ktor-client-android:${Version.ktor}"
    const val ktorIOS = "io.ktor:ktor-client-darwin:${Version.ktor}"
  }

  object SqlDelight {
    const val sqldelight = "com.squareup.sqldelight:runtime:${Version.sqldelight}"
    const val sqldelightExt = "com.squareup.sqldelight:coroutines-extensions:${Version.sqldelight}"
    const val sqldelightAndroid = "com.squareup.sqldelight:android-driver:${Version.sqldelight}"
    const val sqldelightIOS = "com.squareup.sqldelight:native-driver:${Version.sqldelight}"
  }

  const val feedparser = "io.github.lazy-engineer:feedparser:${Version.feedparser}"

  object Compose {
    const val composeRuntime = "androidx.compose.runtime:runtime:${Version.compose}"
    const val composeUi = "androidx.compose.ui:ui:${Version.compose}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Version.compose}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Version.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Version.compose}"
    const val composeMaterialIconsCore = "androidx.compose.material:material-icons-core:${Version.compose}"
    const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended:${Version.compose}"
    const val composeActivity = "androidx.activity:activity-compose:${Version.composeActivity}"
    const val composeLifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.ktxLifecycleRuntime}"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Version.composeViewModel}"
    const val composeNavigation = "androidx.navigation:navigation-compose:${Version.composeNavigation}"
    const val composeUiTests = "androidx.compose.ui:ui-test-junit4:${Version.compose}"
  }

  const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
  const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
}

object TestLibrary {

  const val junit = "junit:junit:${Version.junit}"
}

object AndroidTestLibrary {

  const val androidJunit = "androidx.test.ext:junit:${Version.androidJunit}"
  const val composeUiTest = "androidx.compose.ui:ui-test:${Version.compose}"
  const val composeUiTestJunit = "androidx.compose.ui:ui-test-junit4:${Version.compose}"
  const val composeDebugTestManifest = "androidx.compose.ui:ui-test-manifest:${Version.compose}"
}
