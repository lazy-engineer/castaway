package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.MainScope
import io.github.lazyengineer.castaway.shared.entity.Episode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NativeGetStoredEpisodesUseCase(
  private val getStoredEpisodesUseCase: GetStoredEpisodesUseCase
) {

  val coroutineScope = MainScope(Dispatchers.Main)

  fun subscribe(
	episodeIds: List<String>,
	scope: CoroutineScope,
	onSuccess: (List<Episode>) -> Unit,
	onError: (String) -> Unit,
  ) = getStoredEpisodesUseCase(episodeIds).subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
