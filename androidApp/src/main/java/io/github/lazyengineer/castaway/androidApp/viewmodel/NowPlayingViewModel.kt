package io.github.lazyengineer.castaway.androidApp.viewmodel

import androidx.lifecycle.ViewModel
import io.github.lazyengineer.castaway.androidApp.usecase.StoreAndGetFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.StoredEpisodeFlowableUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient

class NowPlayingViewModel constructor(
  private val mediaServiceClient: MediaServiceClient,
  private val getStoredFeedUseCase: GetStoredFeedUseCase,
  private val storedEpisodeFlowableUseCase: StoredEpisodeFlowableUseCase,
  private val storeAndGetFeedUseCase: StoreAndGetFeedUseCase,
) : ViewModel() {

}