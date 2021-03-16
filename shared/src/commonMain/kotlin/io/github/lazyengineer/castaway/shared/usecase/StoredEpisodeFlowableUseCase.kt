package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.isFrozen
import io.github.lazyengineer.castaway.shared.common.FlowableUseCaseWrapper
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class StoredEpisodeFlowableUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(podcastUrl: String) = FlowableUseCaseWrapper<Episode, String> {
	println("StoredEpisodeFlowableUseCase: ${feedRepository.isFrozen}")
	feedRepository.episodeFlow(podcastUrl)
  }
}