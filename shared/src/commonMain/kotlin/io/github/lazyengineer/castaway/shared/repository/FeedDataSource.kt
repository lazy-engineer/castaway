package io.github.lazyengineer.castaway.shared.repository

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData

interface FeedDataSource {

	suspend fun saveFeed(feed: FeedData): Result<FeedData>
	suspend fun saveEpisode(episode: Episode): Result<Episode>
	suspend fun fetchFeed(url: String): Result<String>
	suspend fun loadLocally(url: String): Result<FeedData>
}
