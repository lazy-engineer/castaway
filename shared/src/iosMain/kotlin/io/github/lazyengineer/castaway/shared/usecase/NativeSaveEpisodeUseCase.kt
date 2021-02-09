package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.MainScope
import io.github.lazyengineer.castaway.shared.entity.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeSaveEpisodeUseCase : KoinComponent {

  private val saveEpisodeUseCase: SaveEpisodeUseCase by inject()
  private val coroutineScope = MainScope(Dispatchers.Main)

  init {
	ensureNeverFrozen()
  }

  fun run(
	episode: Episode,
	onSuccess: (Episode) -> Unit,
	onError: (String) -> Unit,
  ) {
	coroutineScope.launch {
	  saveEpisodeUseCase(
		episode,
		onSuccess = { onSuccess(it) },
		onError = { onError(it.message ?: "Error save") })
	}
  }

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}