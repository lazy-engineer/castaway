package io.github.lazyengineer.castaway.shared.di

import io.github.lazyengineer.castaway.domain.usecase.NativeGetFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeGetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeGetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeSaveEpisodeUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeSaveFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeStoredEpisodeFlowableUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class KoinIOS : KoinComponent {

  fun provideGetFeedUseCase(): NativeGetFeedUseCase = get()
  fun provideGetStoredEpisodesUseCase(): NativeGetStoredEpisodesUseCase = get()
  fun provideGetStoredFeedUseCase(): NativeGetStoredFeedUseCase = get()
  fun provideSaveEpisodeUseCase(): NativeSaveEpisodeUseCase = get()
  fun provideSaveFeedUseCase(): NativeSaveFeedUseCase = get()
  fun provideStoredEpisodeFlowableUseCase(): NativeStoredEpisodeFlowableUseCase = get()
}
