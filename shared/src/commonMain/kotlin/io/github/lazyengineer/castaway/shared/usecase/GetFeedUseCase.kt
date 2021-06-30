package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.UseCaseWrapper
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class GetFeedUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(url: String) = UseCaseWrapper<String, String> {
	feedRepository.fetchXml(url)
  }
}
