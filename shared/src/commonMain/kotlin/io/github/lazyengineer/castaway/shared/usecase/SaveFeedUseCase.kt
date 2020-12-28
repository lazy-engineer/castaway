package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCase
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class SaveFeedUseCase constructor(
	private val feedRepository: FeedDataSource
) : UseCase<FeedData, FeedData>() {

    override suspend fun run(feed: FeedData): Result<FeedData> {
        return feedRepository.saveFeed(feed)
    }
}
