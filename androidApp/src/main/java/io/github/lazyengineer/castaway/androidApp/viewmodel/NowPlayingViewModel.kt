package io.github.lazyengineer.castaway.androidApp.viewmodel

import androidx.lifecycle.ViewModel
import io.github.lazyengineer.castaway.domain.usecase.StoreAndGetFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.StoredEpisodeFlowableUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient

class NowPlayingViewModel constructor(
  private val mediaServiceClient: MediaServiceClient,
  private val getStoredFeedUseCase: GetStoredFeedUseCase,
  private val storedEpisodeFlowableUseCase: StoredEpisodeFlowableUseCase,
  private val storeAndGetFeedUseCase: StoreAndGetFeedUseCase,
) : ViewModel() {

}
