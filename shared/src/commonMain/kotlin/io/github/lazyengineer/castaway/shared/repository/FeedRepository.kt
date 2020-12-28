package io.github.lazyengineer.castaway.shared.repository

import io.github.lazyengineer.castaway.shared.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.Result.Error
import io.github.lazyengineer.castaway.shared.common.Result.Success
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
		return loadRemotely(url)

		/*
		when (val localFeed = localDataSource.fetchFeed(url)) {
			is Success -> localFeed
			is Error -> loadRemotely(url)
		}
		 */
	}

	private suspend fun loadRemotely(url: String): Result<String> {
		return remoteDataSource.fetchFeed(url)

		/*
		when (val fetchedFeed = remoteDataSource.fetchFeed(url)) {
			is Success -> {
				localDataSource.saveFeedData(fetchedFeed.data)
				localDataSource.fetchFeed(remoteFeed.title)
			}
			is Error -> {
				Error(Exception())
			}
		}
		*/
	}
}
