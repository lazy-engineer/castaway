package io.github.lazyengineer.castaway.shared.di

import io.github.lazyengineer.castaway.shared.usecase.NativeGetFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.NativeGetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.shared.usecase.NativeGetStoredFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.NativeLoadImageUseCase
import io.github.lazyengineer.castaway.shared.usecase.NativeSaveEpisodeUseCase
import io.github.lazyengineer.castaway.shared.usecase.NativeSaveFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.NativeStoredEpisodeFlowableUseCase
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.dsl.module

actual val platformModule = module {
  single { NativeGetFeedUseCase(get()) }
  single { NativeGetStoredEpisodesUseCase(get()) }
  single { NativeGetStoredFeedUseCase(get()) }
  single { NativeLoadImageUseCase(get()) }
  single { NativeSaveEpisodeUseCase(get()) }
  single { NativeSaveFeedUseCase(get()) }
  single { NativeStoredEpisodeFlowableUseCase(get()) }
}

class NativeComponent : KoinComponent {

  fun provideGetFeedUseCase(): NativeGetFeedUseCase = get()
  fun provideGetStoredEpisodesUseCase(): NativeGetStoredEpisodesUseCase = get()
  fun provideGetStoredFeedUseCase(): NativeGetStoredFeedUseCase = get()
  fun provideLoadImageUseCase(): NativeLoadImageUseCase = get()
  fun provideSaveEpisodeUseCase(): NativeSaveEpisodeUseCase = get()
  fun provideSaveFeedUseCase(): NativeSaveFeedUseCase = get()
  fun provideStoredEpisodeFlowableUseCase(): NativeStoredEpisodeFlowableUseCase = get()
}