package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource

class GetFeedUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  suspend operator fun invoke(url: String): DataResult<String> {
	return feedRepository.fetchXml(url)
  }
}
