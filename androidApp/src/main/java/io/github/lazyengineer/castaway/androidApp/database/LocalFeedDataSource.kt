package io.github.lazyengineer.castaway.androidApp.database

import io.github.lazyengineer.castaway.androidApp.common.Result
import io.github.lazyengineer.castaway.androidApp.entity.Episode
import io.github.lazyengineer.castaway.androidApp.entity.FeedData

interface LocalFeedDataSource {

	suspend fun fetchFeed(feedUrl: String): Result<FeedData>
	suspend fun saveFeedData(feed: FeedData): Result<FeedData>
	suspend fun saveEpisode(episode: Episode): Result<Episode>
}
