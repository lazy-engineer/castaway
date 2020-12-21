buildscript {
	val kotlin_version by extra("1.4.21")
	val gradle_version by extra("4.1.1")
	val koin_version by extra("2.2.2")

	repositories {
		gradlePluginPortal()
		jcenter()
		google()
		mavenCentral()
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
		classpath("com.android.tools.build:gradle:$gradle_version")
		classpath("org.koin:koin-gradle-plugin:$koin_version")
	}
}

allprojects {
	repositories {
		google()
		jcenter()
		mavenCentral()
	}
}