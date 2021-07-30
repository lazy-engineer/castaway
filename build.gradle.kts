buildscript {
  val kotlinVersion by extra("1.5.10")
  val gradleVersion by extra("7.0.0")
  val sqldelightVersion by extra("1.4.3")

  repositories {
	gradlePluginPortal()
	google()
	mavenCentral()
  }

  dependencies {
	classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
	classpath("com.android.tools.build:gradle:$gradleVersion")
	classpath("com.squareup.sqldelight:gradle-plugin:$sqldelightVersion")
  }
}

allprojects {
  repositories {
	google()
	mavenCentral()
	maven(url = "https://dl.bintray.com/ekito/koin")
  }
}
