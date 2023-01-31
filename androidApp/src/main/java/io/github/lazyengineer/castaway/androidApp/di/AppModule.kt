package io.github.lazyengineer.castaway.androidApp.di

import android.content.ComponentName
import coil.ImageLoader
import coil.memory.MemoryCache
import io.github.lazyengineer.castaway.androidApp.player.FastForwardPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlaybackSpeedUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.PreparePlayerUseCase
import io.github.lazyengineer.castaway.androidApp.player.RewindPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.SeekToUseCase
import io.github.lazyengineer.castaway.androidApp.player.SubscribeToPlayerUseCase
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingViewModel
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastViewModel
import io.github.lazyengineer.castawayplayer.MediaService
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.service.MediaPlayerService
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  single {
	ImageLoader.Builder(androidApplication())
	  .memoryCache {
		MemoryCache.Builder(androidApplication())
		  .maxSizePercent(0.25)
		  .build()
	  }
	  .crossfade(true)
	  .build()
  }

  single {
	MediaService.getInstance(
	  androidContext(),
	  ComponentName(androidContext(), MediaPlayerService::class.java),
	  MediaServiceConfig(rewindInterval = 30_000)
	)
  }

  single { SubscribeToPlayerUseCase(get()) }
  single { PreparePlayerUseCase(get()) }
  single { PlayerStateUseCase(get()) }
  single { PlayPauseUseCase(get()) }
  single { SeekToUseCase(get()) }
  single { FastForwardPlaybackUseCase(get()) }
  single { RewindPlaybackUseCase(get()) }
  single { PlaybackSpeedUseCase(get()) }

  viewModel {
	PodcastViewModel(
	  getStoredFeedUseCase = get(),
	  storeAndGetFeedUseCase = get(),
	  subscribeToPlayerUseCase = get(),
	  preparePlayerUseCase = get(),
	  playerStateUseCase = get(),
	  playPauseUseCase = get()
	)
  }
  viewModel {
	NowPlayingViewModel(
	  playerStateUseCase = get(),
	  saveEpisodeUseCase = get(),
	  getStoredEpisodesUseCase = get(),
	  playPauseUseCase = get(),
	  seekToUseCase = get(),
	  fastForwardPlaybackUseCase = get(),
	  rewindPlaybackUseCase = get(),
	  playbackSpeedUseCase = get()
	)
  }
}
