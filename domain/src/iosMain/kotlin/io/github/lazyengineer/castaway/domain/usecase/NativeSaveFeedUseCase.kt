package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.common.MainScope
import io.github.lazyengineer.castaway.domain.common.UseCaseWrapper
import io.github.lazyengineer.castaway.domain.entity.FeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NativeSaveFeedUseCase(
  private val saveFeedUseCase: SaveFeedUseCase
) {

  val coroutineScope = MainScope(Dispatchers.Main)

  fun subscribe(
    feedData: FeedData,
    scope: CoroutineScope,
    onSuccess: (FeedData) -> Unit,
    onError: (String) -> Unit,
  ) = UseCaseWrapper<FeedData, FeedData> {
    saveFeedUseCase(feedData)
  }.subscribe(scope, onSuccess, onError)

  fun onDestroy() {
    coroutineScope.onDestroy()
  }
}
