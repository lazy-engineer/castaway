package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.common.MainScope
import io.github.lazyengineer.castaway.domain.common.UseCaseWrapper
import io.github.lazyengineer.castaway.domain.entity.Episode
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
  ) = UseCaseWrapper<List<Episode>, List<String>> {
    getStoredEpisodesUseCase(episodeIds)
  }.subscribe(scope, onSuccess, onError)

  fun onDestroy() {
    coroutineScope.onDestroy()
  }
}
