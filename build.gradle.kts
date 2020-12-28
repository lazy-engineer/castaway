buildscript {
	val kotlinVersion by extra("1.4.21")
	val gradleVersion by extra("4.1.1")
	val koinVersion by extra("3.0.0-alpha-4")

	repositories {
		gradlePluginPortal()
		jcenter()
		google()
		mavenCentral()
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("com.android.tools.build:gradle:$gradleVersion")
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