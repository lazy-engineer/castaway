package config

object Shared {

  const val namespace = "io.github.lazyengineer.castaway.shared"

  const val compileSdk = App.compileSdk
  const val minSdk = App.minSdk
  const val targetSdk = App.targetSdk

  val javaVersion = App.javaVersion

  object Data {

    const val namespace = "io.github.lazyengineer.castaway.data"

    const val database = "CastawayDatabase"
    const val databasePackageName = "io.github.lazyengineer.castaway.db"
  }

  object Domain {

    const val namespace = "io.github.lazyengineer.castaway.domain"
  }
}
