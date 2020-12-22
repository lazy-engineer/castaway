package io.github.lazyengineer.castaway.androidApp

import io.github.lazyengineer.castaway.androidApp.entity.FeedData

class GetFeedUseCase constructor(
	private val feedRepository: FeedDataSource
) : UseCase<FeedData, String>() {

	override suspend fun run(url: String): Result<FeedData> {
		return feedRepository.fetchFeed(url)
	}
}