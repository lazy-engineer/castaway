package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.common.MainScope
import io.github.lazyengineer.castaway.domain.common.UseCaseWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NativeGetFeedUseCase(
  private val getFeedUseCase: GetFeedUseCase
) {

  val coroutineScope = MainScope(Dispatchers.Main)

  fun subscribe(
	url: String,
	scope: CoroutineScope,
	onSuccess: (String) -> Unit,
	onError: (String) -> Unit,
  ) = UseCaseWrapper<String, String> {
	getFeedUseCase(url)
  }.subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
