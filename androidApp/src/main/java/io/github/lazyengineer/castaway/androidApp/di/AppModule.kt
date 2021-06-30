package io.github.lazyengineer.castaway.androidApp.di

import android.content.ComponentName
import coil.ImageLoader
import io.github.lazyengineer.castaway.androidApp.usecase.StoreAndGetFeedUseCase
import io.github.lazyengineer.castaway.androidApp.view.player.CastawayPlayer
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.shared.repository.FeedRepository
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.service.MediaPlayerService
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  single {
	ImageLoader.Builder(androidApplication())
	  .availableMemoryPercentage(0.25)
	  .crossfade(true)
	  .build()
  }

  single {
	MediaServiceClient.getInstance(
	  androidContext(),
	  ComponentName(androidContext(), MediaPlayerService::class.java),
	  MediaServiceConfig(rewindInterval = 30_000)
	)
  }

  single {
	CastawayPlayer(get())
  }

  single {
	StoreAndGetFeedUseCase(get() as FeedRepository)
  }

  viewModel { CastawayViewModel(get(), get(), get(), get()) }
}
