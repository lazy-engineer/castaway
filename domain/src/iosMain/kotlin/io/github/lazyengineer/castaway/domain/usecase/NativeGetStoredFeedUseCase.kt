package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.common.MainScope
import io.github.lazyengineer.castaway.domain.common.UseCaseWrapper
import io.github.lazyengineer.castaway.domain.entity.FeedData
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
  ) = UseCaseWrapper<FeedData, String> {
	getStoredFeedUseCase(url)
  }.subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
