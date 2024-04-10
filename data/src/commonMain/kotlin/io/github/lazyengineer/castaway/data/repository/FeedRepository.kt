package io.github.lazyengineer.castaway.data.repository

import io.github.lazyengineer.castaway.data.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.data.webservice.RemoteFeedDataSource
import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Success
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FeedRepository(
  private val remoteDataSource: RemoteFeedDataSource,
  private val localDataSource: LocalFeedDataSource,
) : FeedDataSource {

  override suspend fun saveFeed(feed: FeedData): DataResult<FeedData> {
    return localDataSource.saveFeedData(feed.info, feed.episodes)
  }

  override suspend fun saveEpisode(episode: Episode): DataResult<Episode> {
    return localDataSource.saveEpisode(episode)
  }

  override suspend fun fetchXml(url: String): DataResult<String> {
    return remoteDataSource.fetchFeed(url)
  }

  override suspend fun loadLocally(url: String): DataResult<FeedData> {
    return localDataSource.loadFeed(url)
  }

  override suspend fun loadEpisodes(episodeIds: List<String>): DataResult<List<Episode>> {
    return localDataSource.loadEpisodes(episodeIds)
  }

  override fun episodeFlow(episodeIds: List<String>) = flow {
    localDataSource.episodeFlow(episodeIds).map {
      it.forEach { entity ->
        emit(Success(entity))
      }
    }
  }

  override fun episodeFlow(podcastUrl: String) = flow {
    localDataSource.episodeFlow(podcastUrl).map {
      it.forEach { entity ->
        emit(Success(entity))
      }
    }
  }
}
