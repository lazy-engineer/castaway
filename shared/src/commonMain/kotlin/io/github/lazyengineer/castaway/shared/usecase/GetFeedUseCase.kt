package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCase
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class GetFeedUseCase constructor(
	private val feedRepository: FeedDataSource
) : UseCase<String, String>() {

	override suspend fun run(url: String): Result<String> {
		return feedRepository.fetchFeed(url)
	}
}