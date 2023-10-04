package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource
import kotlinx.coroutines.flow.Flow

class StoredEpisodeFlowableUseCase(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(podcastUrl: String): Flow<DataResult<Episode>> {
	return feedRepository.episodeFlow(podcastUrl)
  }
}
