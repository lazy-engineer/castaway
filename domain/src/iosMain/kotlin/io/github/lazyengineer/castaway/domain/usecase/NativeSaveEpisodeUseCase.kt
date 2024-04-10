package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.common.MainScope
import io.github.lazyengineer.castaway.domain.common.UseCaseWrapper
import io.github.lazyengineer.castaway.domain.entity.Episode
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
  ) = UseCaseWrapper<Episode, Episode> {
    saveEpisodeUseCase(episode)
  }.subscribe(scope, onSuccess, onError)

  fun onDestroy() {
    coroutineScope.onDestroy()
  }
}
