package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.FlowableUseCase
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource
import kotlinx.coroutines.flow.Flow

class StoredEpisodeFlowableUseCase constructor(
  private val feedRepository: FeedDataSource
) : FlowableUseCase<Episode, String>() {

  override fun run(podcastUrl: String): Flow<Result<Episode>> {
	return feedRepository.episodeFlow(podcastUrl)
  }
}