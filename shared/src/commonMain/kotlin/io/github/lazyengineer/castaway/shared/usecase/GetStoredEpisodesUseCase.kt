package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.isFrozen
import io.github.lazyengineer.castaway.shared.common.UseCaseWrapper
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class GetStoredEpisodesUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(episodeIds: List<String>) = UseCaseWrapper<List<Episode>, List<String>> {
	println("GetStoredEpisodesUseCase: ${feedRepository.isFrozen}")
	feedRepository.loadEpisodes(episodeIds)
  }
}