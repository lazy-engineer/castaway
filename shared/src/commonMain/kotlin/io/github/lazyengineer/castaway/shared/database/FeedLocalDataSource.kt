package io.github.lazyengineer.castaway.shared.database

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.Result.Error
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData

class FeedLocalDataSource : LocalFeedDataSource {

	override suspend fun fetchFeed(feedUrl: String): Result<FeedData> {
		return Error(Exception("Not implemented"))
	}

	override suspend fun saveFeedData(feed: FeedData): Result<FeedData> {
		return Error(Exception("Not implemented"))
	}

	override suspend fun saveEpisode(episode: Episode): Result<Episode> {
		return Error(Exception("Not implemented"))
	}
}

const val CASTAWAY_PREFERENCES_NAME = "castaway_prefs"
