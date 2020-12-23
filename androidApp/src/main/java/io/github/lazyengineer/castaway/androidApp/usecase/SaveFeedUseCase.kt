package io.github.lazyengineer.castaway.androidApp.usecase

import io.github.lazyengineer.castaway.androidApp.common.Result
import io.github.lazyengineer.castaway.androidApp.common.UseCase
import io.github.lazyengineer.castaway.androidApp.entity.FeedData
import io.github.lazyengineer.castaway.androidApp.repository.FeedDataSource

class SaveFeedUseCase constructor(
	private val feedRepository: FeedDataSource
) : UseCase<FeedData, FeedData>() {

	override suspend fun run(feed: FeedData): Result<FeedData> {
		return feedRepository.saveFeed(feed)
	}
}
