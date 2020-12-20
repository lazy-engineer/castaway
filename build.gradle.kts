buildscript {
	val kotlin_version by extra("1.4.21")
	val gradle_version by extra("4.1.1")
	val compile_sdk by extra(30)
	val target_sdk by extra(30)
	val min_sdk by extra(21)

	val build_tools_version by extra("30.0.2")
	val version_code by extra(1)
	val version_name by extra("1.0")
	val test_runner by extra("androidx.test.runner.AndroidJUnitRunner")

	val ktx_core_version by extra("1.3.2")
	val coroutines_version by extra("1.4.1")
	val exoplayer_version by extra("2.12.0")
	val coil_version by extra("1.1.0")
	val gson_version by extra("2.8.6")

	val junit_version by extra("4.13.1")
	val android_junit_version by extra("1.1.2")

	repositories {
		gradlePluginPortal()
		jcenter()
		google()
		mavenCentral()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
		classpath("com.android.tools.build:gradle:$gradle_version")
	}
}

allprojects {
	repositories {
		google()
		jcenter()
		mavenCentral()
	}
}