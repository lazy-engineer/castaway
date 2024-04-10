plugins {
  id("io.gitlab.arturbosch.detekt") version ("1.23.1")
}

buildscript {
  val kotlinVersion by extra("1.9.10")
  val gradleVersion by extra("8.1.1")
  val sqldelightVersion by extra("1.5.3")

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

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

subprojects {
  apply(plugin = "io.gitlab.arturbosch.detekt")

  detekt {
    toolVersion = "1.23.1"
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    baseline = file("$rootDir/config/detekt/baseline.xml")
    source.from(
      files(
        "src/androidTest/java",
        "src/androidTest/kotlin",
        "src/androidMain/kotlin",
        "src/commonMain/kotlin",
        "src/iosMain/kotlin",
        "src/iosTest/kotlin",
      )
    )
    parallel = true
  }

  plugins.withType<org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper> {
    dependencies {
      detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }
  }

  tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.majorVersion
    reports {
      xml.required.set(true)
      xml.outputLocation.set(file("$rootDir/build/reports/detekt.xml"))
    }
  }

  tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.majorVersion
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
      if (project.findProperty("composeCompilerReports") == "true") {
        freeCompilerArgs += listOf(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_metrics"
        )
      }
      if (project.findProperty("composeCompilerMetrics") == "true") {
        freeCompilerArgs += listOf(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_metrics"
        )
      }
    }
  }
}

val detektAutoCorrect by tasks.registering(io.gitlab.arturbosch.detekt.Detekt::class) {
  description = "Tries to auto correct detected issues."
  group = "detekt"
  autoCorrect = true
  buildUponDefaultConfig = true
  parallel = true
  setSource(files(rootDir))
  config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
  baseline.set(file("$rootDir/config/detekt/baseline.xml"))
  include("**/*.kt")
  include("**/*.kts")
  exclude("**/resources/**")
  exclude("**/build/**")
}
