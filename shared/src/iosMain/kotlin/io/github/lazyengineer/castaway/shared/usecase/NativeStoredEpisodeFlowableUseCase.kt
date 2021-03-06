package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.MainScope
import io.github.lazyengineer.castaway.shared.entity.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeStoredEpisodeFlowableUseCase : KoinComponent {

  private val storedEpisodeFlowable: StoredEpisodeFlowableUseCase by inject()
  private val coroutineScope = MainScope(Dispatchers.Main)

  init {
	ensureNeverFrozen()
  }

  fun run(
	feedUrl: String,
	onEach: (Episode) -> Unit,
	onError: (String) -> Unit,
	onComplete: () -> Unit
  ) {
	coroutineScope.launch {
	  storedEpisodeFlowable(
		feedUrl,
		onEach = { onEach(it) },
		onError = { onError(it.message ?: "Error save") },
		onComplete = { onComplete() }
	  ).collect()
	}
  }

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}