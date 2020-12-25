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
  const val kotlin = "1.4.21"
  const val gradle = "4.1.1"

  const val appcompat = "1.1.0"
  const val constraintlayout = "2.0.4"
  const val material = "1.2.1"
  const val media = "1.2.1"

  const val compose = "1.0.0-alpha08"

  const val lifecycleExtensions = "2.2.0"
  const val ktxActivity = "1.1.0"
  const val ktxFragment = "1.2.5"

  const val ktxCore = "1.3.2"
  const val coroutines = "1.4.1"
  const val exoplayer = "2.12.0"
  const val coil = "1.1.0"
  const val gson = "2.8.6"
  const val koin = "2.2.2"
  const val okhttp = "4.7.2"
  const val feedparser = "0.1.0"

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

  const val compose = "androidx.compose.ui:ui:${Version.compose}"
  const val composeTooling = "androidx.compose.ui:ui-tooling:${Version.compose}"
  const val composeFoundation = "androidx.compose.foundation:foundation:${Version.compose}"
  const val composeMaterial = "androidx.compose.material:material:${Version.compose}"
  const val composeIcons = "androidx.compose.material:material-icons-core:${Version.compose}"
  const val composeIconsExt = "androidx.compose.material:material-icons-extended:${Version.compose}"

  const val koin = "org.koin:koin-core:${Version.koin}"
  const val koinExt = "org.koin:koin-core-ext:${Version.koin}"
  const val koinAndroid = "org.koin:koin-androidx-scope:${Version.koin}"
  const val koinViewmodel = "org.koin:koin-androidx-viewmodel:${Version.koin}"

  const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycleExtensions}"
  const val activityKtx = "androidx.activity:activity-ktx:${Version.ktxActivity}"
  const val fragmentKtx = "androidx.fragment:fragment-ktx:${Version.ktxFragment}"

  const val media = "androidx.media:media:${Version.media}"
  const val exoplayer = "com.google.android.exoplayer:exoplayer:${Version.exoplayer}"
  const val exoplayerMediaSession = "com.google.android.exoplayer:extension-mediasession:${Version.exoplayer}"
  const val exoplayerCast = "com.google.android.exoplayer:extension-cast:${Version.exoplayer}"
  const val exoplayerUI = "com.google.android.exoplayer:exoplayer-ui:${Version.exoplayer}"
  const val coil = "io.coil-kt:coil:${Version.coil}"
  const val gson = "com.google.code.gson:gson:${Version.gson}"
  const val okhttp = "com.squareup.okhttp3:okhttp:${Version.okhttp}"
  const val feedparser = "io.github.lazy-engineer:feedparser:${Version.feedparser}"

  const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
  const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
}

object TestLibrary {
  const val junit = "junit:junit:${Version.junit}"
}

object AndroidTestLibrary {
  const val androidJunit = "androidx.test.ext:junit:${Version.androidJunit}"
}