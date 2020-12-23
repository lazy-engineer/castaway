package io.github.lazyengineer.castaway.androidApp.repository

import io.github.lazyengineer.castaway.androidApp.common.Result
import io.github.lazyengineer.castaway.androidApp.entity.Episode
import io.github.lazyengineer.castaway.androidApp.entity.FeedData

interface FeedDataSource {

	suspend fun saveFeed(feed: FeedData): Result<FeedData>
	suspend fun saveEpisode(episode: Episode): Result<Episode>
	suspend fun fetchFeed(url: String): Result<FeedData>
}
