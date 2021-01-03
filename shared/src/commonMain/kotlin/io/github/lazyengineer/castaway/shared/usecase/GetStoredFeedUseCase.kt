package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCase
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class GetStoredFeedUseCase constructor(
    private val feedRepository: FeedDataSource
) : UseCase<FeedData, String>() {

    override suspend fun run(feedUrl: String): Result<FeedData> {
        return feedRepository.loadLocally(feedUrl)
    }
}