package io.github.lazyengineer.castaway.androidApp

import android.app.Application
import io.github.lazyengineer.castaway.androidApp.di.appModule
import io.github.lazyengineer.castaway.shared.database.appContext
import io.github.lazyengineer.castaway.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class App : Application() {

  override fun onCreate() {
	super.onCreate()

	appContext = this

	initKoin {
	  androidLogger()
	  androidContext(this@App)
	  modules(appModule)
	}
  }
}
