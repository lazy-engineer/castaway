package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource

class GetStoredFeedUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  suspend operator fun invoke(feedUrl: String): DataResult<FeedData> {
	return feedRepository.loadLocally(feedUrl)
  }
}
