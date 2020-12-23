package io.github.lazyengineer.castaway.androidApp.usecase

import io.github.lazyengineer.castaway.androidApp.common.Result
import io.github.lazyengineer.castaway.androidApp.common.UseCase
import io.github.lazyengineer.castaway.androidApp.entity.FeedData
import io.github.lazyengineer.castaway.androidApp.repository.FeedDataSource

class GetFeedUseCase constructor(
	private val feedRepository: FeedDataSource
) : UseCase<FeedData, String>() {

	override suspend fun run(url: String): Result<FeedData> {
		return feedRepository.fetchFeed(url)
	}
}