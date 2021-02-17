package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.MainScope
import io.github.lazyengineer.castaway.shared.entity.FeedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeSaveFeedUseCase : KoinComponent {

  private val saveFeedUseCase: SaveFeedUseCase by inject()
  private val coroutineScope = MainScope(Dispatchers.Main)

  init {
	ensureNeverFrozen()
  }

  fun run(
	feedData: FeedData,
	onSuccess: (FeedData) -> Unit,
	onError: (String) -> Unit,
  ) {
	coroutineScope.launch {
	  saveFeedUseCase(
		feedData,
		onSuccess = { onSuccess(it) },
		onError = { onError(it.message ?: "Error save") })
	}
  }

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}