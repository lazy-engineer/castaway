import config.CastawayPlayer

plugins {
  alias(libs.plugins.detekt)
}

buildscript {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }

  dependencies {
    classpath(libs.gradle)
    classpath(libs.kotlin.gradle.plugin)
    classpath(libs.sqldelight.gradle.plugin)
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
  apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)

  detekt {
    toolVersion = rootProject.libs.plugins.detekt.get().version.requiredVersion
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
      detektPlugins(libs.detekt)
    }
  }

  tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = CastawayPlayer.jvmTarget
    reports {
      xml.required.set(true)
      xml.outputLocation.set(file("$rootDir/build/reports/detekt.xml"))
    }
  }

  tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = CastawayPlayer.jvmTarget
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
