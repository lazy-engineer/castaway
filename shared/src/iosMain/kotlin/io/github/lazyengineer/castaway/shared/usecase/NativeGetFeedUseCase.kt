package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.MainScope
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
  ) = getFeedUseCase(url).subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
