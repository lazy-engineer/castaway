package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.MainScope
import io.github.lazyengineer.castaway.shared.entity.FeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NativeGetStoredFeedUseCase(
  private val getStoredFeedUseCase: GetStoredFeedUseCase
) {

  val coroutineScope = MainScope(Dispatchers.Main)

  fun subscribe(
	url: String,
	scope: CoroutineScope,
	onSuccess: (FeedData) -> Unit,
	onError: (String) -> Unit,
  ) = getStoredFeedUseCase(url).subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
