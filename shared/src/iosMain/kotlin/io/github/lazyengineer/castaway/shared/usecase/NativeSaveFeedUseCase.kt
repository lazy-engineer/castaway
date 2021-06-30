package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.MainScope
import io.github.lazyengineer.castaway.shared.entity.FeedData
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
  ) = saveFeedUseCase(feedData).subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
