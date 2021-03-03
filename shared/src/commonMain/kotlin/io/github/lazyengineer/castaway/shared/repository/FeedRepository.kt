package io.github.lazyengineer.castaway.shared.repository

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.webservice.ImageLoader
import io.github.lazyengineer.castaway.shared.webservice.RemoteFeedDataSource

class FeedRepository constructor(
  private val imageLoader: ImageLoader,
  private val remoteDataSource: RemoteFeedDataSource,
  private val localDataSource: LocalFeedDataSource,
) : FeedDataSource {

  init {
	ensureNeverFrozen()
  }

  override suspend fun saveFeed(feed: FeedData): Result<FeedData> {
	var feedToStore = feed

	if (feed.image == null) {
	  feed.imageUrl?.let { feedImageUrl ->
		feedToStore = when (val imageResult = imageLoader.loadImage(feedImageUrl)) {
		  is Result.Success -> feed.copy(image = imageResult.data)
		  is Result.Error -> feed
		}
	  }
	}

	return localDataSource.saveFeedData(feedToStore)
  }

  override suspend fun saveEpisode(episode: Episode): Result<Episode> {
	var episodeToStore = episode

	if (episode.image == null) {
	  episode.imageUrl?.let { episodeImageUrl ->
		episodeToStore = when (val imageResult = imageLoader.loadImage(episodeImageUrl)) {
		  is Result.Success -> episode.copy(image = imageResult.data)
		  is Result.Error -> episode
		}
	  }
	}

	return localDataSource.saveEpisode(episodeToStore)
  }

  override suspend fun fetchXml(url: String): Result<String> {
	return remoteDataSource.fetchFeed(url)
  }

  override suspend fun loadLocally(url: String): Result<FeedData> {
	return localDataSource.loadFeed(url)
  }

  override suspend fun loadEpisodes(episodeIds: List<String>): Result<List<Episode>> {
	return localDataSource.loadEpisodes(episodeIds)
  }
}
