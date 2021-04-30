buildscript {
  val kotlinVersion by extra("1.4.32")
  val gradleVersion by extra("4.1.2")
  val sqldelightVersion by extra("1.4.3")

  repositories {
	gradlePluginPortal()
	jcenter()
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
	jcenter()
	mavenCentral()
	maven(url = "https://dl.bintray.com/ekito/koin")
  }
}