package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource

class GetStoredEpisodesUseCase(
  private val feedRepository: FeedDataSource
) {

  suspend operator fun invoke(episodeIds: List<String>): DataResult<List<Episode>> {
	return feedRepository.loadEpisodes(episodeIds)
  }
}
