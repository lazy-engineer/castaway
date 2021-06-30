package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.MainScope
import io.github.lazyengineer.castaway.shared.entity.Episode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NativeSaveEpisodeUseCase(
  private val saveEpisodeUseCase: SaveEpisodeUseCase
) {

  val coroutineScope = MainScope(Dispatchers.Main)

  fun subscribe(
	episode: Episode,
	scope: CoroutineScope,
	onSuccess: (Episode) -> Unit,
	onError: (String) -> Unit,
  ) = saveEpisodeUseCase(episode).subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
