package io.github.lazyengineer.castaway.shared.repository

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.Result.Error
import io.github.lazyengineer.castaway.shared.common.Result.Success
import io.github.lazyengineer.castaway.shared.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.webservice.RemoteFeedDataSource

class FeedRepository constructor(
	private val remoteDataSource: RemoteFeedDataSource,
	private val localDataSource: LocalFeedDataSource
) : FeedDataSource {

	override suspend fun saveFeed(feed: FeedData): Result<FeedData> {
		return localDataSource.saveFeedData(feed)
	}

	override suspend fun saveEpisode(episode: Episode): Result<Episode> {
		return localDataSource.saveEpisode(episode)
	}

	override suspend fun fetchFeed(url: String): Result<String> {
		return remoteDataSource.fetchFeed(url)
	}

	override suspend fun loadLocally(url: String): Result<FeedData> {
		return when (val localFeed = localDataSource.loadFeed(url)) {
			is Success -> localFeed
			is Error -> Error(Exception())
		}
	}
}
