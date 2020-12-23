package io.github.lazyengineer.castaway.androidApp

import android.app.Application
import io.github.lazyengineer.castaway.androidApp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.DEBUG

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidLogger(DEBUG)
			androidContext(this@App)
			modules(appModule)
		}
	}
}