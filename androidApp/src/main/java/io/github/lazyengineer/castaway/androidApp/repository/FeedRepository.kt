package io.github.lazyengineer.castaway.androidApp.repository

import io.github.lazyengineer.castaway.shared.Result
import io.github.lazyengineer.castaway.shared.Result.Error
import io.github.lazyengineer.castaway.shared.Result.Success
import io.github.lazyengineer.castaway.androidApp.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.androidApp.entity.Episode
import io.github.lazyengineer.castaway.androidApp.entity.FeedData
import io.github.lazyengineer.castaway.androidApp.entity.PlaybackPosition
import io.github.lazyengineer.castaway.androidApp.webservice.RemoteFeedDataSource
import io.github.lazyengineer.feedparser.model.feed.AtomFeed
import io.github.lazyengineer.feedparser.model.feed.Feed
import io.github.lazyengineer.feedparser.model.feed.RSSFeed
import java.util.UUID

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

	override suspend fun fetchFeed(url: String): Result<FeedData> {
		return when (val localFeed = localDataSource.fetchFeed(url)) {
			is Success -> localFeed
			is Error -> loadRemotely(url)
		}
	}

	private suspend fun loadRemotely(url: String): Result<FeedData> {
		return when (val fetchedFeed = remoteDataSource.fetchFeed(url)) {
			is Success -> {
				val remoteFeed = fetchedFeed.data.toFeedData(url)
				localDataSource.saveFeedData(remoteFeed)
				localDataSource.fetchFeed(remoteFeed.title)
			}
			is Error -> {
				Error(Exception())
			}
		}
	}

	private fun Feed.toFeedData(url: String): FeedData {
		return when (this) {
			is RSSFeed -> {
				FeedData(url = url, title = this.title, episodes = this.items.map {
					Episode(
						id = UUID.randomUUID().toString(),
						title = it.title,
						subTitle = it.iTunes?.subtitle ?: "",
						description = it.description,
						audioUrl = it.media?.url ?: "",
						imageUrl = it.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url ?: "",
						author = it.author ?: "",
						playbackPosition = PlaybackPosition(0, Long.MAX_VALUE),
					)
				})
			}
			is AtomFeed -> {
				FeedData(url = url, title = this.title?.value ?: "", episodes = this.entries.map {
					Episode(
						id = UUID.randomUUID().toString(),
						title = it.title?.value ?: "",
						subTitle = "",
						description = it.summary?.value ?: "",
						audioUrl = it.mediaNamespace?.contents?.firstOrNull()?.attributes?.url ?: "",
						imageUrl = it.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url ?: "",
						author = it.authors.firstOrNull()?.uri ?: "",
						playbackPosition = PlaybackPosition(0, Long.MAX_VALUE),
					)
				})
			}
		}
	}
}
