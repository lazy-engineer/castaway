pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}
rootProject.name = "castaway"

include(":castawayplayer")
include(":androidApp")
include(":shared")
include(":domain")
include(":data")
