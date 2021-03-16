package io.github.lazyengineer.castaway.shared.database

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.FeedInfo
import io.github.lazyengineer.castaway.shared.ext.toEpisode
import io.github.lazyengineer.castaway.shared.ext.toEpisodeEntity
import io.github.lazyengineer.castaway.shared.fromNativeImage
import io.github.lazyengineer.castaway.shared.toNativeImage
import iogithublazyengineercastawaydb.EpisodeEntity
import iogithublazyengineercastawaydb.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FeedLocalDataSource constructor(private val database: CastawayDatabase) :
  LocalFeedDataSource {

  override suspend fun loadFeedInfo(feedUrl: String): Result<FeedInfo> {
	return database.episodeQueries.transactionWithResult {
	  try {
		val podcast = database.podcastQueries.selectByUrl(feedUrl).executeAsOne()

		Result.Success(
		  FeedInfo(
			url = podcast.url,
			title = podcast.name,
			imageUrl = podcast.imageUrl,
			image = podcast.image?.toNativeImage(),
		  )
		)
	  } catch (e: NullPointerException) {
		Result.Error(e)
	  }
	}
  }

  override suspend fun loadFeed(feedUrl: String): Result<FeedData> {
	return database.episodeQueries.transactionWithResult {
	  try {
		val podcast = database.podcastQueries.selectByUrl(feedUrl).executeAsOne()

		val episodes = database.episodeQueries.selectByPodcast(feedUrl).executeAsList().map {
		  it.toEpisode()
		}

		Result.Success(
		  FeedData(
			FeedInfo(
			  url = podcast.url,
			  title = podcast.name,
			  imageUrl = podcast.imageUrl,
			  image = podcast.image?.toNativeImage(),
			),
			episodes = episodes
		  )
		)
	  } catch (e: NullPointerException) {
		Result.Error(e)
	  }
	}
  }

  override suspend fun loadEpisodes(episodeIds: List<String>): Result<List<Episode>> {
	return database.episodeQueries.transactionWithResult {
	  try {
		val episodes = database.episodeQueries.selectByIds(episodeIds).executeAsList().map {
		  it.toEpisode()
		}

		Result.Success(episodes)
	  } catch (e: NullPointerException) {
		Result.Error(e)
	  }
	}
  }

  override fun episodeFlow(episodeIds: List<String>): Flow<List<Episode>> {
	return database.episodeQueries.selectByIds(
	  episodeIds,
	  mapper = { id, title, subTitle, description, audioUrl, imageUrl, image, author, playbackPosition, episode, podcastUrl ->
		EpisodeEntity(
		  id = id,
		  title = title,
		  subTitle = subTitle,
		  description = description,
		  audioUrl = audioUrl,
		  imageUrl = imageUrl,
		  image = image,
		  author = author,
		  playbackPosition = playbackPosition,
		  episode = episode,
		  podcastUrl = podcastUrl,
		).toEpisode()
	  }
	).asFlow()
	  .mapToList()
  }

  override fun episodeFlow(podcastUrl: String): Flow<List<Episode>> {
	return database.episodeQueries.selectByPodcast(
	  podcastUrl,
	  mapper = { id, title, subTitle, description, audioUrl, imageUrl, image, author, playbackPosition, episode, podcastUrl ->
		EpisodeEntity(
		  id = id,
		  title = title,
		  subTitle = subTitle,
		  description = description,
		  audioUrl = audioUrl,
		  imageUrl = imageUrl,
		  image = image,
		  author = author,
		  playbackPosition = playbackPosition,
		  episode = episode,
		  podcastUrl = podcastUrl,
		).toEpisode()
	  }
	).asFlow()
	  .mapToList()
  }

  override suspend fun saveFeedData(feed: FeedData): Result<FeedData> {
	val savedFeed: FeedData = database.episodeQueries.transactionWithResult {
	  database.podcastQueries.insertPodcast(
		Podcast(
		  url = feed.info.url,
		  name = feed.info.title,
		  imageUrl = feed.info.imageUrl,
		  image = feed.info.image?.fromNativeImage()
		)
	  )

	  feed
	}

	insertEpisodes(feed.episodes.map { it })

	return Result.Success(savedFeed)
  }

  private suspend fun insertEpisodes(episodes: List<Episode>) {
	episodes.forEach {
	  saveEpisode(it)
	}
  }

  override suspend fun saveEpisode(episode: Episode): Result<Episode> {
	val savedEpisode: Episode = database.episodeQueries.transactionWithResult {
	  val episodeEntity = episode.toEpisodeEntity()
	  database.episodeQueries.insertEpisode(
		episodeEntity
	  )

	  episodeEntity.toEpisode()
	}

	return Result.Success(savedEpisode)
  }

  private suspend fun <R> Transacter.transactionWithContext(
	coroutineContext: CoroutineContext,
	noEnclosing: Boolean = false,
	body: TransactionWithReturn<R>.() -> R
  ): R {
	return withContext(coroutineContext) {
	  this@transactionWithContext.transactionWithResult(noEnclosing) {
		body()
	  }
	}
  }
}
