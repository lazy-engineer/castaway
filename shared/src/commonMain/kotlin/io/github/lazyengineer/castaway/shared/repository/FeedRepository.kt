package io.github.lazyengineer.castaway.shared.repository

import io.github.lazyengineer.castaway.shared.Image
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.Result.Error
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

  override suspend fun saveFeed(feed: FeedData): Result<FeedData> {
	var feedToStore = feed

	if (feed.info.image == null) {
	  feed.info.imageUrl?.let { feedImageUrl ->
		feedToStore = when (val imageResult = imageLoader.loadImage(feedImageUrl)) {
		  is Success -> feed.copy(info = feed.info.copy(image = imageResult.data))
		  is Error -> feed
		}
	  }
	}

	return localDataSource.saveFeedData(feedToStore)
  }

  override suspend fun saveEpisode(episode: Episode): Result<Episode> {
	var episodeToStore = episode

	if (episode.image == null) {
	  episodeToStore = episode.loadImage()
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

  private suspend fun FeedData.copyWithAllImages(imageResult: Success<Image>) =
	this.copy(info = this.info.copy(image = imageResult.data), episodes = this.episodes.map {
	  if (it.image == null) {
		it.loadImage(imageResult.data)
	  } else {
		it
	  }
	})

  private suspend fun Episode.loadImage(feedImage: Image? = null) = when {
	this.imageUrl != null -> this.loadEpisodeImageFromUrl()
	feedImage != null -> this.copy(image = feedImage)
	else -> this.loadEpisodeImageFromFeed()
  }

  private suspend fun Episode.loadEpisodeImageFromUrl() =
	when (val imageResult = this.imageUrl?.let { imageLoader.loadImage(it) }) {
	  is Success -> this.copy(image = imageResult.data)
	  is Error -> this
	  null -> this
	}

  private suspend fun Episode.loadEpisodeImageFromFeed() =
	when (val feedInfo = localDataSource.loadFeedInfo(this.podcastUrl)) {
	  is Success -> this.copy(image = feedInfo.data.image)
	  is Error -> this
	}
}
