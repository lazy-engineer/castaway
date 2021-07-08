buildscript {
  val kotlinVersion by extra("1.4.32")
  val gradleVersion by extra("4.2.2")
  val sqldelightVersion by extra("1.4.3")
  val mokoVersion by extra("0.15.1")

  repositories {
	gradlePluginPortal()
	google()
	mavenCentral()
  }

  dependencies {
	classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
	classpath("com.android.tools.build:gradle:$gradleVersion")
	classpath("com.squareup.sqldelight:gradle-plugin:$sqldelightVersion")
	classpath("dev.icerock.moko:resources-generator:$mokoVersion")
  }
}

allprojects {
  repositories {
	google()
	mavenCentral()
	maven(url = "https://dl.bintray.com/ekito/koin")
  }
}
