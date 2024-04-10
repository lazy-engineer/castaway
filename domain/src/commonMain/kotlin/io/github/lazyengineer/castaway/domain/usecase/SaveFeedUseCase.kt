package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource

class SaveFeedUseCase(
  private val feedRepository: FeedDataSource
) {

  suspend operator fun invoke(feed: FeedData): DataResult<FeedData> {
    return feedRepository.saveFeed(feed)
  }
}
