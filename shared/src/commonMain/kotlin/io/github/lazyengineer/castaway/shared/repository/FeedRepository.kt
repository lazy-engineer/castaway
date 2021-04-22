package io.github.lazyengineer.castaway.shared.repository

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.Result.Success
import io.github.lazyengineer.castaway.shared.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.webservice.ImageLoader
import io.github.lazyengineer.castaway.shared.webservice.RemoteFeedDataSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class FeedRepository constructor(
  private val imageLoader: ImageLoader,
  private val remoteDataSource: RemoteFeedDataSource,
  private val localDataSource: LocalFeedDataSource,
) : FeedDataSource {

  init {
	ensureNeverFrozen()
  }

  override suspend fun saveFeed(feed: FeedData): Result<FeedData> {
	return localDataSource.saveFeedData(feed.info, feed.episodes)
  }

  override suspend fun saveEpisode(episode: Episode): Result<Episode> {
	return localDataSource.saveEpisode(episode)
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

  override fun episodeFlow(episodeIds: List<String>) = flow {
	localDataSource.episodeFlow(episodeIds).collect {
	  it.forEach { entity ->
		emit(Success(entity))
	  }
	}
  }

  override fun episodeFlow(podcastUrl: String) = flow {
	localDataSource.episodeFlow(podcastUrl).collect {
	  it.forEach { entity ->
		emit(Success(entity))
	  }
	}
  }
}
