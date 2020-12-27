package io.github.lazyengineer.castaway.androidApp.di

import android.content.ComponentName
import android.content.Context
import coil.ImageLoader
import com.google.gson.Gson
import io.github.lazyengineer.castaway.androidApp.database.CASTAWAY_PREFERENCES_NAME
import io.github.lazyengineer.castaway.androidApp.database.FeedLocalDataSource
import io.github.lazyengineer.castaway.androidApp.repository.FeedRepository
import io.github.lazyengineer.castaway.androidApp.usecase.GetFeedUseCase
import io.github.lazyengineer.castaway.androidApp.viewmodel.MainViewModel
import io.github.lazyengineer.castaway.shared.webservice.FeedRemoteDataSource
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.service.MediaPlayerService
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

	single {
		androidContext().getSharedPreferences(
			CASTAWAY_PREFERENCES_NAME,
			Context.MODE_PRIVATE
		)
	}
	single { Gson() }
	single { OkHttpClient() }
	single {
		ImageLoader.Builder(androidApplication())
			.availableMemoryPercentage(0.25)
			.crossfade(true)
			.build()
	}

	single {
		FeedRepository(
			remoteDataSource = FeedRemoteDataSource(),
			localDataSource = FeedLocalDataSource.getInstance(get(), get())
		)
	}
	single { GetFeedUseCase(get() as FeedRepository) }
	single {
		MediaServiceClient.getInstance(
			androidContext(),
			ComponentName(androidContext(), MediaPlayerService::class.java),
			MediaServiceConfig(fastForwardInterval = 10_000)
		)
	}

	viewModel { MainViewModel(get(), get()) }
}