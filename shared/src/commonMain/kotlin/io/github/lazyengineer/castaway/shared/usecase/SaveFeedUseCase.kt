package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.isFrozen
import io.github.lazyengineer.castaway.shared.common.UseCaseWrapper
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class SaveFeedUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(feed: FeedData) = UseCaseWrapper<FeedData, FeedData> {
	println("SaveFeedUseCase: ${feedRepository.isFrozen}")
	feedRepository.saveFeed(feed)
  }
}
