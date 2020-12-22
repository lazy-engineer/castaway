buildscript {
	val kotlinVersion by extra("1.4.21")
	val gradleVersion by extra("4.1.1")
	val koinVersion by extra("2.2.2")

	repositories {
		gradlePluginPortal()
		jcenter()
		google()
		mavenCentral()
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("com.android.tools.build:gradle:$gradleVersion")
		classpath("org.koin:koin-gradle-plugin:$koinVersion")
	}
}

allprojects {
	repositories {
		google()
		jcenter()
		mavenCentral()
	}
}