package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.MainScope
import io.github.lazyengineer.castaway.shared.entity.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeGetStoredEpisodesUseCase : KoinComponent {

  private val getStoredEpisodesUseCase: GetStoredEpisodesUseCase by inject()
  private val coroutineScope = MainScope(Dispatchers.Main)

  init {
	ensureNeverFrozen()
  }

  fun run(
	episodeIds: List<String>,
	onSuccess: (List<Episode>) -> Unit,
	onError: (String) -> Unit,
  ) {
	coroutineScope.launch {
	  getStoredEpisodesUseCase(
		episodeIds,
		onSuccess = { onSuccess(it) },
		onError = { onError(it.message ?: "Error fetch") })
	}
  }

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
