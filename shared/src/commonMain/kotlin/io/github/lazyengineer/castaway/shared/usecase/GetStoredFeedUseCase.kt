package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.UseCaseWrapper
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class GetStoredFeedUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(feedUrl: String) = UseCaseWrapper<FeedData, String> {
	feedRepository.loadLocally(feedUrl)
  }
}