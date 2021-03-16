package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.MainScope
import io.github.lazyengineer.castaway.shared.entity.Episode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NativeStoredEpisodeFlowableUseCase(
  private val storedEpisodeFlowable: StoredEpisodeFlowableUseCase
) {

  val coroutineScope = MainScope(Dispatchers.Main)

  fun subscribe(
	feedUrl: String,
	scope: CoroutineScope,
	onEach: (Episode) -> Unit,
	onError: (String) -> Unit,
	onComplete: () -> Unit
  ) = storedEpisodeFlowable(feedUrl).subscribe(scope, onEach, onError, onComplete)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}