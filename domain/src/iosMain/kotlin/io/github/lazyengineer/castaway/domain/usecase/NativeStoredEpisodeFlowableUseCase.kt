package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.common.FlowableUseCaseWrapper
import io.github.lazyengineer.castaway.domain.common.MainScope
import io.github.lazyengineer.castaway.domain.entity.Episode
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
  ) = FlowableUseCaseWrapper<Episode, String> {
	storedEpisodeFlowable(feedUrl)
  }.subscribe(scope, onEach, onError, onComplete)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
